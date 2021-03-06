package com.kunize.uswtimetable.ui.evaluation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kunize.uswtimetable.NavGraphDirections
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.databinding.FragmentEvaluationBinding
import com.kunize.uswtimetable.ui.common.EventObserver
import com.kunize.uswtimetable.ui.common.User
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.login.LoginActivity
import com.kunize.uswtimetable.util.FragmentType
import com.kunize.uswtimetable.util.LectureItemViewType
import com.kunize.uswtimetable.util.TextLength.MIN_SEARCH_TEXT_LENGTH
import com.kunize.uswtimetable.util.TimeTableSelPref

class EvaluationFragment : Fragment() {
    lateinit var binding: FragmentEvaluationBinding
    private lateinit var evaluationFooterAdapter: EvaluationFooterAdapter
    private val args: EvaluationFragmentArgs by navArgs()
    private val evaluationViewModel: EvaluationViewModel by viewModels { ViewModelFactory() }
    private var imageSortDialog: ImageSortDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        evaluationViewModel.majorType = TimeTableSelPref.prefs.getString("openMajorSel", "전체")
        evaluationViewModel.changeType(args.sortType)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_evaluation, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        evaluationFooterAdapter = EvaluationFooterAdapter { id ->
            if (id == LectureItemViewType.FOOTER.toLong()) {
                goToSearchResult("", evaluationViewModel.spinnerSel)
                return@EvaluationFooterAdapter
            }
            if(User.isLoggedIn.value == true) {
                val action = NavGraphDirections.actionGlobalLectureInfoFragment(lectureId = id)
                findNavController().navigate(action)
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }

        binding.tvSelectedOpenMajor.text = evaluationViewModel.majorType

        binding.recyclerEvaluation.adapter = evaluationFooterAdapter

        binding.viewModel = evaluationViewModel
        binding.lifecycleOwner = this

        binding.btnSearch.setOnClickListener {
            if (isSearchTextLengthNotEnough()) return@setOnClickListener
            goToSearchResult()
        }

        binding.clOpenMajor.setOnClickListener {
            goToSelectOpenMajorFragment()
        }

        //키보드 검색 클릭 시 프래그먼트 이동 이벤트 구현
        binding.etSearch.setOnEditorActionListener { _, it, _ ->
            var handled = false
            if (it == EditorInfo.IME_ACTION_SEARCH && !isSearchTextLengthNotEnough()) {
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                goToSearchResult(now = evaluationViewModel.spinnerTextList.indexOf(evaluationViewModel.sortText.value))
                handled = true
            }
            handled
        }

        //spinner 설정
        binding.clSort.setOnClickListener {
            imageSortDialog = ImageSortDialog(context as AppCompatActivity, evaluationViewModel)
            imageSortDialog?.show()
        }

        evaluationViewModel.dialogItemClickEvent.observe(viewLifecycleOwner, EventObserver {
            imageSortDialog?.dismiss()
        })

        evaluationViewModel.toastViewModel.toastLiveData.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(
                requireContext(),
                evaluationViewModel.toastViewModel.toastMessage,
                Toast.LENGTH_LONG
            ).show()
        })

    }

    private fun isSearchTextLengthNotEnough(): Boolean {
        if (binding.etSearch.text.toString().length < MIN_SEARCH_TEXT_LENGTH) {
            Toast.makeText(requireContext(), "2글자 이상 입력해주세요!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun goToSearchResult(
        msg: String = binding.etSearch.text.toString(),
        now: Int = 0
    ) {
        val action =
            EvaluationFragmentDirections.actionNavigationEvaluationToSearchResultFragment(msg, now)
        findNavController().navigate(action)
    }

    private fun goToSelectOpenMajorFragment() {
        val action =
            EvaluationFragmentDirections.globalOpenMajor(FragmentType.EVALUATION,
                prevSortType = evaluationViewModel.spinnerTextList.indexOf(evaluationViewModel.sortText.value))
        findNavController().navigate(action)
    }
}