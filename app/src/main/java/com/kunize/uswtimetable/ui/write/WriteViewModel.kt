package com.kunize.uswtimetable.ui.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunize.uswtimetable.data.remote.LectureEvaluationEditDto
import com.kunize.uswtimetable.data.remote.LectureEvaluationPostDto
import com.kunize.uswtimetable.data.remote.LectureExamDto
import com.kunize.uswtimetable.repository.write.WriteRepository
import com.kunize.uswtimetable.ui.common.Event
import com.kunize.uswtimetable.ui.common.HandlingErrorInterface
import com.kunize.uswtimetable.ui.common.ToastViewModel
import kotlinx.coroutines.withContext
import retrofit2.Response

class WriteViewModel(private val writeRepository: WriteRepository) : ViewModel(), HandlingErrorInterface {
    val toastViewModel = ToastViewModel()
    private val _honeyScore = MutableLiveData<Float>()
    val honeyScore: LiveData<Float>
        get() = _honeyScore

    private val _semesterText = MutableLiveData<String>()
    val semesterText: LiveData<String>
        get() = _semesterText

    private val _testText = MutableLiveData<String>()
    val testText: LiveData<String>
        get() = _testText

    private val _dialogSemesterClickEvent = MutableLiveData<Event<Boolean>>()
    val dialogSemesterClickEvent: LiveData<Event<Boolean>>
        get() = _dialogSemesterClickEvent

    private val _dialogTestClickEvent = MutableLiveData<Event<Boolean>>()
    val dialogTestClickEvent: LiveData<Event<Boolean>>
        get() = _dialogTestClickEvent

    fun initSemesterText(text: String) {
        _semesterText.value = text
    }

    fun initTestText(text: String) {
        _testText.value = text
    }

    fun dialogSemesterClick(text: String) {
        _semesterText.value = text
        _dialogSemesterClickEvent.value = Event(true)
    }

    fun dialogTestClick(text: String) {
        _testText.value = text
        _dialogTestClickEvent.value = Event(true)
    }

    fun changeHoneyScore(value: Float) {
        _honeyScore.value = value
    }

    private val _satisfactionScore = MutableLiveData<Float>()
    val satisfactionScore: LiveData<Float>
        get() = _satisfactionScore

    fun changeSatisfactionScore(value: Float) {
        _satisfactionScore.value = value
    }

    private val _learningScore = MutableLiveData<Float>()
    val learningScore: LiveData<Float>
        get() = _learningScore

    fun changeLearningScore(value: Float) {
        _learningScore.value = value
    }

    suspend fun postLectureEvaluation(lectureId: Long, info: LectureEvaluationPostDto): Response<String> {
        val response: Response<String>
        withContext(viewModelScope.coroutineContext) {
            response = writeRepository.postLectureEvaluation(lectureId, info)
        }
        return response
    }

    suspend fun postLectureExam(lectureId: Long, info: LectureExamDto): Response<String> {
        val response: Response<String>
        withContext(viewModelScope.coroutineContext) {
            response = writeRepository.postLectureExam(lectureId, info)
        }
        return response
    }

    suspend fun updateLectureEvaluation(lectureId: Long, info: LectureEvaluationEditDto): Response<String> {
        val response: Response<String>
        withContext(viewModelScope.coroutineContext) {
            response = writeRepository.updateLectureEvaluation(lectureId, info)
        }
        return response
    }

    suspend fun updateLectureExam(lectureId: Long, info: LectureExamDto): Response<String> {
        val response: Response<String>
        withContext(viewModelScope.coroutineContext) {
            response = writeRepository.updateLectureExam(lectureId, info)
        }
        return response
    }

    init {
        _honeyScore.value = 0f
        _satisfactionScore.value = 0f
        _learningScore.value = 0f
    }

    override fun handleError(errorCode: Int) {
        toastViewModel.toastMessage = when (errorCode) {
            400 -> "?????? ????????? ????????? ?????????!"
            403 -> "????????? ?????????! ?????? ?????? ????????? ??????????????? ??????????????????!"
            else -> "$errorCode ?????? ??????!"
        }
        toastViewModel.showToastMsg()
    }
}