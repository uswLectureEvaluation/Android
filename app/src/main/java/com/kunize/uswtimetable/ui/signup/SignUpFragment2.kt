package com.kunize.uswtimetable.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.databinding.FragmentSignUp2Binding
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.util.afterTextChanged

class SignUpFragment2 : Fragment() {
    private var _binding: FragmentSignUp2Binding? = null
    val binding: FragmentSignUp2Binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    private lateinit var activity: SignUpActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up2, container, false)

        activity = requireActivity() as SignUpActivity

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        viewModel.isEmailUnique.observe(viewLifecycleOwner) { unique ->
            if (!unique && viewModel.errorMessage.value.isNullOrBlank().not()) {
                activity.makeToast(viewModel.errorMessage.value!!)
            }
        }

        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (!success && viewModel.errorMessage.value.isNullOrBlank().not()) {
                activity.makeToast(viewModel.errorMessage.value!!)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.etMail.afterTextChanged {
            dataChanged()
        }
    }

    private fun dataChanged() {
        viewModel.setNextButtonEnable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}