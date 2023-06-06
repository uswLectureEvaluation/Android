package com.kunize.uswtimetable.ui.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kunize.uswtimetable.databinding.FragmentNoticeDetailBinding
import com.kunize.uswtimetable.ui.common.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoticeDetailFragment : Fragment() {
    private var _binding: FragmentNoticeDetailBinding? = null
    private val binding: FragmentNoticeDetailBinding get() = _binding!!
    private val viewModel: NoticeDetailViewModel by viewModels()

    private val navArgs: NoticeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNoticeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        viewModel.getNotice(navArgs.noticeId)
        viewModel.notice.observe(viewLifecycleOwner) {
            binding.notice = it
        }

        viewModel.eventBack.observe(
            viewLifecycleOwner,
            EventObserver {
                if (it) findNavController().navigateUp()
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
