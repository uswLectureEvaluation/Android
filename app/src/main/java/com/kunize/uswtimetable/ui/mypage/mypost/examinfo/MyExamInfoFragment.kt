package com.kunize.uswtimetable.ui.mypage.mypost.examinfo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.data.remote.LectureExamDto
import com.kunize.uswtimetable.databinding.FragmentMyExamInfoBinding
import com.kunize.uswtimetable.ui.common.User
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.lecture_info.LectureInfoFragmentDirections
import com.kunize.uswtimetable.ui.mypage.mypost.Result
import com.kunize.uswtimetable.util.UserPoint
import com.kunize.uswtimetable.util.extensions.infiniteScrolls
import com.kunize.uswtimetable.util.extensions.repeatOnStarted
import com.kunize.uswtimetable.util.extensions.toast
import kotlin.math.abs

class MyExamInfoFragment : Fragment() {
    private var _binding: FragmentMyExamInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val viewModel: MyExamInfoViewModel by viewModels { ViewModelFactory() }

    private lateinit var adapter: MyExamInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyExamInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        adapter = MyExamInfoAdapter(viewModel)

        initRecyclerView()

        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiEvent.collect { event ->
                handleEvent(event)
            }
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.resultFlow.collect { result ->
                if (result == Result.Fail) {
                    if ((User.user?.value?.point ?: 0) < abs(UserPoint.DELETE_POST)) {
                        this@MyExamInfoFragment.toast("???????????? ???????????????")
                    } else {
                        this@MyExamInfoFragment.toast("????????? ??? ????????????")
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.myExamInfoContainer
        recyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { myExamInfoList ->
            adapter.submitList(myExamInfoList)
        }

        recyclerView.infiniteScrolls {
            viewModel.scrollBottomEvent()
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.EditEvent -> gotoWriteFragment(event.examInfo)
            is Event.DeleteEvent -> showAlertDialog(event.examInfo)
        }
    }

    private fun showAlertDialog(data: LectureExamDto) {
        AlertDialog.Builder(requireContext())
            .setMessage("??????????????? ???????????? 30P??? ?????? ?????????.\n?????????????????????????")
            .setNeutralButton("??????") { _, _ -> }
            .setPositiveButton("??????") { _, _ ->
                data.id?.let { id ->
                    viewModel.deletePost(id)
                }
            }
            .show()
    }

    private fun gotoWriteFragment(data: LectureExamDto) {
        data.id ?: return
        val action =
            LectureInfoFragmentDirections.actionGlobalWriteFragment(
                lectureId = data.id,
                myExamInfo = data,
                isEvaluation = false
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
