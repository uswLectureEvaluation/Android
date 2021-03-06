package com.kunize.uswtimetable.ui.signup

import android.os.Bundle
import android.text.InputFilter
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.databinding.FragmentSignUp1Binding
import com.kunize.uswtimetable.ui.common.ViewModelFactory
import com.kunize.uswtimetable.util.Constants
import com.kunize.uswtimetable.util.Constants.KEY_URL
import com.kunize.uswtimetable.util.Constants.PRIVACY_POLICY_SITE
import com.kunize.uswtimetable.util.Constants.TERMS_SITE
import com.kunize.uswtimetable.util.extensions.afterTextChanged
import java.util.regex.Pattern

class SignUpFragment1 : Fragment() {
    private var _binding: FragmentSignUp1Binding? = null
    val binding: FragmentSignUp1Binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up1, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        viewModel.signupFormState.observe(viewLifecycleOwner) {
            val state = it ?: return@observe

            state.idError?.let { errMsg ->
                binding.layoutInputId.error = resources.getString(errMsg)
            }
            state.pwError?.let { errMsg ->
                binding.layoutInputPw.error = resources.getString(errMsg)
            }
            state.pwAgainError?.let { errMsg ->
                binding.layoutInputPwAgain.error = resources.getString(errMsg)
            }
        }

        viewModel.isIdUnique.observe(viewLifecycleOwner) { isUnique ->
            if (isUnique == false && viewModel.errorMessage.value?.peekContent().isNullOrBlank().not()) {
                viewModel.setToastMessage(viewModel.errorMessage.value!!.peekContent())
            }
        }

        initViews()
    }

    private fun initViews() {

        with(binding) {
            etInputId.afterTextChanged {
                viewModel.showCheckIdButton()
                dataChanged()
                inputLimitAlert(it.toString(), Constants.ID_COUNT_LIMIT)
            }
            etInputPw.afterTextChanged {
                dataChanged()
                inputLimitAlert(it.toString(), Constants.PW_COUNT_LIMIT)
            }
            etInputPwAgain.afterTextChanged {
                dataChanged()
                inputLimitAlert(it.toString(), Constants.PW_COUNT_LIMIT)
            }
            cbTerms.setOnClickListener {
                dataChanged()
            }

            // ?????????: ???????????? ????????? ?????? ??????
            etInputId.filters = arrayOf(
                InputFilter { source, _, _, _, _, _ ->
                    val idRegex = """^[a-z0-9]*$"""
                    val idPattern = Pattern.compile(idRegex)
                    if (source.isNullOrBlank() || idPattern.matcher(source).matches()) {
                        return@InputFilter source
                    }
                    viewModel.setToastMessage("???????????? ????????? ?????? ???????????????: $source")
                    source.dropLast(1)
                }
            )

            // ????????????: ?????????, ??????, ?????? ?????? ?????? ?????? ??????
            etInputPw.filters = arrayOf(
                InputFilter { source, _, _, _, _, _ ->
                    val pwRegex = """^[0-9a-zA-Z!@#$%^+\-=]*$"""
                    val pwPattern = Pattern.compile(pwRegex)
                    if (source.isNullOrBlank() || pwPattern.matcher(source).matches()) {
                        return@InputFilter source
                    }
                    viewModel.setToastMessage("????????? ??? ?????? ???????????????: $source")
                    ""
                }
            )
        }
        /* ?????? ?????? ?????? ?????? */
        val link1 = Pattern.compile("????????????")
        val link2 = Pattern.compile("????????????????????????")
        val mTransform = Linkify.TransformFilter { _, _ -> "" }
        Linkify.addLinks(binding.tvTerms, link1, "suwiki://web_view?$KEY_URL=$TERMS_SITE", null, mTransform)
        Linkify.addLinks(binding.tvTerms, link2, "suwiki://web_view?$KEY_URL=$PRIVACY_POLICY_SITE", null, mTransform)
    }

    private fun dataChanged() {
        viewModel.signUpDataChanged(
            id = binding.etInputId.text.toString(),
            pw = binding.etInputPw.text.toString(),
            pwAgain = binding.etInputPwAgain.text.toString(),
            term = binding.cbTerms.isChecked
        )
    }

    private fun inputLimitAlert(str: String, limit: Int) {
        if (str.length >= limit) {
            viewModel.setToastMessage("????????? ??? ?????? ?????? ???????????????")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
