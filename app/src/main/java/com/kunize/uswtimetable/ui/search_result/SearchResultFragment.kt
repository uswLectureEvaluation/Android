package com.kunize.uswtimetable.ui.search_result

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
import com.kunize.uswtimetable.databinding.FragmentSearchResultBinding
import com.kunize.uswtimetable.ui.common.EventObserver
import com.kunize.uswtimetable.ui.common.User
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.login.LoginActivity
import com.kunize.uswtimetable.util.FragmentType
import com.kunize.uswtimetable.util.TextLength.MIN_SEARCH_TEXT_LENGTH
import com.kunize.uswtimetable.util.TimeTableSelPref
import com.kunize.uswtimetable.util.extensions.infiniteScrolls

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding
    private lateinit var searchResultAdapter: SearchResultAdapter
    private val searchResultViewModel: SearchResultViewModel by viewModels { ViewModelFactory() }
    private val args: SearchResultFragmentArgs by navArgs()
    private var sortDialog: SortDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchResultViewModel.initType()
        searchResultViewModel.majorType = TimeTableSelPref.prefs.getString("openMajorSel", "전체")
        if (args.searchLectureName.isBlank())
            searchResultViewModel.changeType(args.sortType)
        else
            searchResultViewModel.search(args.searchLectureName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        binding.viewModel = searchResultViewModel
        binding.lifecycleOwner = this

        searchResultAdapter = SearchResultAdapter { id ->
            if (User.isLoggedIn.value == true) {
                val action = NavGraphDirections.actionGlobalLectureInfoFragment(lectureId = id)
                findNavController().navigate(action)
            } else {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }

        binding.tvSelectedOpenMajor.text = searchResultViewModel.majorType
        binding.clOpenMajor.setOnClickListener {
            val action =
                SearchResultFragmentDirections.globalOpenMajor(
                    FragmentType.SEARCH_RESULT,
                    searchResultViewModel.searchValue,
                    searchResultViewModel.spinnerTextList.indexOf(searchResultViewModel.sortText.value)
                )
            findNavController().navigate(action)
        }

        binding.recyclerSearchResult.adapter = searchResultAdapter

        binding.recyclerSearchResult.infiniteScrolls {
            searchResultViewModel.scrollBottomEvent()
        }

        binding.clSort.setOnClickListener {
            sortDialog = SortDialog(
                context as AppCompatActivity,
                searchResultViewModel,
                searchResultViewModel.spinnerTextList
            )
            sortDialog?.show()
        }

        searchResultViewModel.dialogItemClickEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                sortDialog?.dismiss()
            }
        )

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.etSearch.apply {
            setText(args.searchLectureName)
            setSelection(args.searchLectureName.length)
        }

        binding.btnSearch.setOnClickListener {
            if (isSearchTextLengthNotEnough()) return@setOnClickListener
            searchResultViewModel.search(binding.etSearch.text.toString())
        }

        binding.etSearch.setOnEditorActionListener { _, it, _ ->
            var handled = false
            if (it == EditorInfo.IME_ACTION_SEARCH && !isSearchTextLengthNotEnough()) {
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                handled = true
                searchResultViewModel.search(binding.etSearch.text.toString())
            }
            handled
        }

        searchResultViewModel.toastViewModel.toastLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                Toast.makeText(
                    requireContext(),
                    searchResultViewModel.toastViewModel.toastMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        return binding.root
    }

    private fun isSearchTextLengthNotEnough(): Boolean {
        if (binding.etSearch.text.toString().length < MIN_SEARCH_TEXT_LENGTH) {
            Toast.makeText(requireContext(), "2글자 이상 입력해주세요!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}
