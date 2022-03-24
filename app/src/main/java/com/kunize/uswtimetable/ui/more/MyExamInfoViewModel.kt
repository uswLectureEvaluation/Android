package com.kunize.uswtimetable.ui.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kunize.uswtimetable.dataclass.MyExamInfo
import com.kunize.uswtimetable.ui.repository.my_post.MyExamInfoRepository

class MyExamInfoViewModel(private val repository: MyExamInfoRepository) : ViewModel() {
    private val _myExamInfoData = MutableLiveData<List<MyExamInfo>>()
    val myExamInfoData: LiveData<List<MyExamInfo>> get() = _myExamInfoData

    init {
        getMyExamInfo().let { data ->
            _myExamInfoData.value = data
        }
    }

    private fun getMyExamInfo(): List<MyExamInfo> {
        return repository.getExamInfos()
    }

    fun getMyExamInfoDetail(id: String): MyExamInfo? = myExamInfoData.value?.find { evaluation ->
        evaluation.id == id
    }
}