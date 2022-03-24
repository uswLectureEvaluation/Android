package com.kunize.uswtimetable.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kunize.uswtimetable.adapter.MyExamInfoAdapter
import com.kunize.uswtimetable.databinding.FragmentMyExamInfoBinding
import com.kunize.uswtimetable.dataclass.MyExamInfo
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.ui.lecture_info.LectureInfoFragmentDirections
import com.kunize.uswtimetable.util.ItemType

class MyExamInfoFragment : Fragment() {
    private var _binding: FragmentMyExamInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyExamInfoViewModel by viewModels { ViewModelFactory(requireContext()) }

    private lateinit var adapter: MyExamInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyExamInfoBinding.inflate(inflater, container, false)

        binding.myExamInfoContainer.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = MyExamInfoAdapter { data, type ->
            when (type) {
                ItemType.ROOT_VIEW -> {
//                    Toast.makeText(requireContext(), "$data 선택됨", Toast.LENGTH_SHORT).show()
                }
                ItemType.EDIT_BUTTON -> {
//                    Toast.makeText(requireContext(), "아이템${data.id} 수정 버튼 선택됨", Toast.LENGTH_SHORT).show()
                    gotoWriteFragment(data)
                }
                ItemType.DELETE_BUTTON -> {
                    Toast.makeText(requireContext(), "아이템${data.id} 삭제 버튼 선택됨", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.myExamInfoContainer.adapter = adapter


        viewModel.myExamInfoData.observe(viewLifecycleOwner) { myExamInfoList ->
            adapter.submitList(myExamInfoList)
        }

        return binding.root
    }

    private fun gotoWriteFragment(data: MyExamInfo) {
        val action =
            LectureInfoFragmentDirections.actionGlobalWriteFragment(
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