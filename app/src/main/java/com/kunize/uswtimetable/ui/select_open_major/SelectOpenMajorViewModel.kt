package com.kunize.uswtimetable.ui.select_open_major

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunize.uswtimetable.ui.common.Event
import com.suwiki.domain.model.OpenMajor
import com.suwiki.domain.repository.OpenMajorRepository
import com.suwiki.domain.usecase.GetUserInfoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectOpenMajorViewModel @Inject constructor(
    private val openMajorRepository: OpenMajorRepository,
    userInfoUsecase: GetUserInfoUsecase,
) : ViewModel() {
    private val _starClickEvent = MutableLiveData<Event<String>>()
    val starClickEvent: LiveData<Event<String>>
        get() = _starClickEvent

    private val _needLoginLayout = MutableLiveData<Boolean>()
    val needLoginLayout: LiveData<Boolean>
        get() = _needLoginLayout

    private val _showNoSearchResultText = MutableLiveData<String>()
    val showNoSearchResultText: LiveData<String>
        get() = _showNoSearchResultText

    val isLoggedIn: StateFlow<Boolean> = userInfoUsecase.isLoggedIn()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            false,
        )

    lateinit var tempOpenMajorList: List<OpenMajor>

    init {
        _showNoSearchResultText.value = ""
        viewModelScope.launch { tempOpenMajorList = openMajorRepository.getLocalOpenMajorList() }
    }

    suspend fun showNeedLoginLayout() = withContext(Main) {
        _needLoginLayout.value = true
    }

    suspend fun hideNeedLoginLayout() = withContext(Main) {
        _needLoginLayout.value = false
    }

    fun setNoSearchResultText(text: String) {
        _showNoSearchResultText.value = text
    }

    fun starClick(data: String) {
        _starClickEvent.value = Event(data)
    }

    suspend fun bookmarkMajor(majorName: String) {
        viewModelScope.launch {
            openMajorRepository.bookmarkMajor(majorName)
        }
    }

    suspend fun clearBookmarkMajor(majorName: String) {
        viewModelScope.launch {
            openMajorRepository.clearBookmarkMajor(majorName)
        }
    }

    suspend fun getBookmarkList(): List<String> =
        openMajorRepository.getBookmarkMajorList().getOrNull() ?: emptyList()
}
