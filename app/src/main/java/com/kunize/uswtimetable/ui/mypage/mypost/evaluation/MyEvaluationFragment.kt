package com.kunize.uswtimetable.ui.mypage.mypost.evaluation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.databinding.FragmentMyEvaluationBinding
import com.kunize.uswtimetable.dataclass.MyEvaluationDto
import com.kunize.uswtimetable.ui.common.User
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.mypage.mypost.Result
import com.kunize.uswtimetable.util.UserPoint.DELETE_POST
import com.kunize.uswtimetable.util.extensions.infiniteScrolls
import com.kunize.uswtimetable.util.extensions.repeatOnStarted
import com.kunize.uswtimetable.util.extensions.toast
import kotlin.math.abs

class MyEvaluationFragment : Fragment() {
    private var _binding: FragmentMyEvaluationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyEvaluationAdapter
    private val viewModel: MyEvaluationViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyEvaluationBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = MyEvaluationAdapter(viewModel)

        initRecyclerView()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiEvent.collect { event ->
                handleEvent(event)
            }
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.resultFlow.collect { result ->
                if (result == Result.Fail) {
                    if ((User.user?.value?.point ?: 0) < abs(DELETE_POST)) {
                        this@MyEvaluationFragment.toast("???????????? ???????????????")
                    } else {
                        this@MyEvaluationFragment.toast("????????? ??? ????????????")
                    }
                }
            }
        }
    }

    private fun gotoWriteFragment(data: MyEvaluationDto) {
        val action =
            MyEvaluationFragmentDirections.actionGlobalWriteFragment(
                lectureId = data.id,
                myEvaluation = data,
                isEvaluation = true
            )
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        recyclerView = binding.rvMyEvaluation
        recyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { evaluations ->
            adapter.submitList(evaluations)
        }
        recyclerView.infiniteScrolls {
            viewModel.scrollBottomEvent()
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.EditEvent -> gotoWriteFragment(event.evaluation)
            is Event.DeleteEvent -> showAlertDialog(event.evaluation)
        }
    }

    private fun showAlertDialog(data: MyEvaluationDto) {
        AlertDialog.Builder(requireContext())
            .setMessage("??????????????? ???????????? 30P??? ?????? ?????????.\n?????????????????????????")
            .setNeutralButton("??????") { _, _ -> }
            .setPositiveButton("??????") { _, _ ->
                viewModel.deletePost(data.id)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
