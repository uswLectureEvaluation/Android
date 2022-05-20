package com.kunize.uswtimetable.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.repository.signup.SignUpRepository
import com.kunize.uswtimetable.ui.common.Event
import com.kunize.uswtimetable.util.Constants
import com.kunize.uswtimetable.util.Constants.SCHOOL_DOMAIN_AT
import com.kunize.uswtimetable.util.Constants.TAG
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel(private val repository: SignUpRepository) : ViewModel() {
    // User Input
    val id = MutableLiveData<String>()
    val pw = MutableLiveData<String>()
    val pwAgain = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val termChecked = MutableLiveData<Boolean>()

    // Button
    val previousButtonVisibility = MutableLiveData<Boolean>()
    val nextButtonEnable = MutableLiveData(false)
    val previousButtonText = MutableLiveData<String>()
    val nextButtonText = MutableLiveData<String>()

    // State
    val loading = MutableLiveData<Boolean>()
    private var _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> get() = _currentPage
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private var _signupForm = MutableLiveData<SignUpFormState>()
    val signupFormState: LiveData<SignUpFormState> get() = _signupForm

    // Response
    val isIdUnique = MutableLiveData(false)
    val isEmailUnique = MutableLiveData(false)
    val signUpResult = MutableLiveData<Boolean>()

    // Pattern
    private val idPattern: Pattern = Pattern.compile(Constants.ID_REGEX)
    private val pwPattern: Pattern = Pattern.compile(Constants.PW_REGEX)

    // Event
    private val _eventCheckMail = MutableLiveData<Event<Boolean>>()
    val eventCheckMail: LiveData<Event<Boolean>> get() = _eventCheckMail
    private val _eventLoginButton = MutableLiveData<Event<Boolean>>()
    val eventLoginButton: LiveData<Event<Boolean>> get() = _eventLoginButton
    private val _eventCloseButton = MutableLiveData<Event<Boolean>>()
    val eventCloseButton: LiveData<Event<Boolean>> get() = _eventCloseButton

    init {
        _currentPage.value = 0
        loading.value = false
        setButtonAttr()
        setNextButtonEnable()
    }

    fun signUpDataChanged(
        id: String,
        pw: String,
        pwAgain: String,
        term: Boolean
    ) {
        when {
            checkIdLength(id).not() -> {
                _signupForm.value = SignUpFormState(idError = R.string.check_id_length)
            }
            isIdValid(id).not() -> {
                _signupForm.value = SignUpFormState(idError = R.string.invalid_id)
            }
            checkPwLength(pw).not() -> {
                _signupForm.value = SignUpFormState(pwError = R.string.check_pw_length)
            }
            isPwValid(pw).not() -> {
                _signupForm.value = SignUpFormState(pwError = R.string.invalid_pw)
            }
            isPwAgainValid(pw, pwAgain).not() -> {
                _signupForm.value = SignUpFormState(pwAgainError = R.string.invalid_pw_again)
            }
            hasBlank(id, pw, pwAgain) -> {
                _signupForm.value = SignUpFormState(hasBlank = R.string.has_blank)
            }
            term.not() -> {
                _signupForm.value = SignUpFormState(isTermChecked = R.string.unchecked_term)
            }
            else -> _signupForm.value = SignUpFormState(isDataValid = true)
        }
        setNextButtonEnable()
    }

    fun onNextButtonClick() {
        when (currentPage.value) {
            0 -> checkId()
            1 -> signUp()
            2 -> _eventLoginButton.value = Event(true)
        }
    }

    fun moveToPreviousPage() {
        _currentPage.value = when (currentPage.value) {
            1 -> {
                isIdUnique.value = false
                _currentPage.value?.minus(1)
            }
            2 -> {
                _eventCloseButton.value = Event(true)
                _currentPage.value
            }
            else -> _currentPage.value
        }
        setButtonAttr()
        setNextButtonEnable()
    }

    fun setNextButtonEnable() {
        nextButtonEnable.value = when (currentPage.value) {
            0 -> (loading.value == false) && (signupFormState.value?.isDataValid == true) // 아이디, 비밀번호, 비밀번호 다시, 약관 동의
            1 -> (loading.value == false) && (isIdUnique.value == true) && (email.value?.isNotBlank() == true) // 이메일
            2 -> true
            else -> false
        }
        Log.d(TAG, "SignUpViewModel - setNextButtonEnable() called / ${nextButtonEnable.value}")
    }

    fun onClickCheckMail() {
        _eventCheckMail.value = Event(true)
    }

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    private fun moveToNextPage() {
        _currentPage.value = when (currentPage.value) {
            0 -> {
                _currentPage.value?.plus(1)
            }
            1 -> {
                _currentPage.value?.plus(1)
            }
            else -> _currentPage.value
        }
        setButtonAttr()
        setNextButtonEnable()
    }

    private fun setButtonAttr() {
        // 페이지 변경 시에만 호출
        when (currentPage.value) {
            0 -> {
                previousButtonText.value = "이전"
                nextButtonText.value = "다음"
                previousButtonVisibility.value = false
            }
            1 -> {
                previousButtonText.value = "이전"
                nextButtonText.value = "회원 가입"
                previousButtonVisibility.value = true
            }
            2 -> {
                previousButtonText.value = "닫기"
                nextButtonText.value = "로그인"
                previousButtonVisibility.value = true
            }
        }
    }



    private fun checkId() {
        val userId = id.value ?: return
        loading.value = true
        viewModelScope.launch {
            val response = repository.checkId(userId)
            if (response.isSuccessful) {
                isIdUnique.postValue(response.body()?.overlap == false)
                if (response.body()?.overlap == true) {
                    onError("이미 가입된 아이디입니다.")
                } else {
                    moveToNextPage()
                }
            } else {
                onError("${response.code()} Error: ${response.errorBody()}")
            }
            loading.postValue(false)
        }
    }

    private fun signUp() {
        val userId = id.value ?: return
        val userPw = pw.value ?: return
        val userEmail = email.value?.plus(SCHOOL_DOMAIN_AT) ?: return
        if (isIdUnique.value != true) {
            onError("이미 가입된 아이디입니다.")
            return
        }
        loading.value = true
        viewModelScope.launch {
            // 이메일 중복 확인
            val emailResponse = repository.checkEmail(userEmail)
            if (emailResponse.isSuccessful) {
                isEmailUnique.postValue(emailResponse.body()?.overlap == false)
                if (emailResponse.body()?.overlap == true) {
                    loading.postValue(false)
                    onError("이미 가입된 이메일입니다.")
                    return@launch
                }
            } else {
                onError("이메일 중복 확인 에러: ${emailResponse.errorBody()}")
                loading.postValue(false)
                return@launch
            }
            // 회원 가입
            val signUpResponse = repository.signUp(userId, userPw, userEmail)
            if (signUpResponse.isSuccessful) {
                signUpResult.postValue(signUpResponse.body()?.success == true)
                if (signUpResponse.body()?.success == true) {
                    moveToNextPage()
                } else {
                    onError("회원 가입 실패: ${signUpResponse.message()}")
                    loading.postValue(false)
                    return@launch
                }
            } else {
                onError("회원 가입 에러: ${signUpResponse.errorBody()}")
                loading.postValue(false)
                return@launch
            }
            loading.postValue(false)
        }
    }

    private fun isIdValid(id: String): Boolean {
        return id.isBlank() || idPattern.matcher(id).matches()
    }

    private fun checkIdLength(id: String): Boolean {
        return id.isBlank() || id.length in Constants.ID_COUNT_LOWER_LIMIT..Constants.ID_COUNT_LIMIT
    }

    private fun isPwValid(pw: String): Boolean {
        return pw.isBlank() || pwPattern.matcher(pw).matches()
    }

    private fun checkPwLength(pw: String): Boolean {
        return pw.isBlank() || pw.length in Constants.PW_COUNT_LOWER_LIMIT..Constants.PW_COUNT_LIMIT
    }

    private fun isPwAgainValid(pw: String, pwAgain: String): Boolean {
        return pw.isBlank() || pwAgain.isBlank() || pw == pwAgain
    }

    private fun hasBlank(
        id: String,
        pw: String,
        pwAgain: String
    ): Boolean {
        return id.isBlank() || pw.isBlank() || pwAgain.isBlank()
    }

    private fun onError(message: String) {
        _errorMessage.value = message
    }
}