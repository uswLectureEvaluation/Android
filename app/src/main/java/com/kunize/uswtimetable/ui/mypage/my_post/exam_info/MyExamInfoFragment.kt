package com.kunize.uswtimetable.ui.mypage.my_post.exam_info

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
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.lecture_info.LectureInfoFragmentDirections
import com.kunize.uswtimetable.util.infiniteScrolls
import com.kunize.uswtimetable.util.repeatOnStarted

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
    }

    private fun initRecyclerView() {
        recyclerView = binding.myExamInfoContainer
        recyclerView.adapter = adapter

        viewModel.myExamInfoData.observe(viewLifecycleOwner) { myExamInfoList ->
            adapter.submitList(myExamInfoList)
        }

        recyclerView.infiniteScrolls {
            viewModel.scrollBottomEvent()
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.EditEvent -> {
                gotoWriteFragment(event.examInfo)
            }
            is Event.DeleteEvent -> {
                // TODO 삭제 확인 다이얼로그 띄우기
                event.examInfo.id?.let { id ->
                    viewModel.deletePost(id)
                }
            }
        }
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
