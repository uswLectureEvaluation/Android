package com.kunize.uswtimetable.ui.mypage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunize.uswtimetable.dataclass.MyExamInfoDto
import com.kunize.uswtimetable.ui.repository.my_post.MyPostRepository
import com.kunize.uswtimetable.util.Constants
import com.kunize.uswtimetable.util.LAST_PAGE
import kotlinx.coroutines.launch

class MyExamInfoViewModel(private val repository: MyPostRepository) : ViewModel() {
    private val _myExamInfoData = MutableLiveData<List<MyExamInfoDto>>()
    val myExamInfoData: LiveData<List<MyExamInfoDto>> get() = _myExamInfoData
    val loading = MutableLiveData<Boolean>()
    private val page = MutableLiveData<Int>()

    init {
        page.value = 1
        loading.value = true
        scrollBottomEvent()
    }
/*
    fun getMyExamInfoDetail(id: String): MyExamInfo? = myExamInfoData.value?.find { evaluation ->
        evaluation.id == id
    }*/

    fun scrollBottomEvent() {
        Log.d(Constants.TAG, "MyEvaluationViewModel - scrollBottomEvent(${page.value}) called")
        if (page.value == LAST_PAGE) return

        viewModelScope.launch {
            val response = repository.getExamInfos(page.value ?: 1)
            if (response.isSuccessful && response.body() != null) {
                val newData = response.body()?.data ?: return@launch
                if (newData.isNullOrEmpty()) return@launch

                val currentData = _myExamInfoData.value?.toMutableList() ?: mutableListOf()
                currentData.addAll(newData)
                _myExamInfoData.postValue(currentData)
                Log.d(Constants.TAG, "MyEvaluationViewModel / new data: $newData ")
                nextPage()
            }
            loading.postValue(false)
        }
    }

    private fun nextPage() {
        if (page.value == LAST_PAGE) return
        page.postValue(page.value?.plus(1))
    }
}