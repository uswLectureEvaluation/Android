package com.kunize.uswtimetable.ui.evaluation

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.NavGraphDirections
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.adapter.EvaluationListAdapter
import com.kunize.uswtimetable.dataclass.EvaluationData

import com.kunize.uswtimetable.ui.search_result.SearchResultFragmentDirections
import okhttp3.internal.notify

open class TempEvaluationViewModel : ViewModel() {
    private val _evaluationList = MutableLiveData<ArrayList<EvaluationData?>>()
    val evaluationList: LiveData<ArrayList<EvaluationData?>>
        get() = _evaluationList


    init {
        _evaluationList.value = arrayListOf()
    }

    fun changeData(dataList : ArrayList<EvaluationData?>) {
        _evaluationList.value = dataList
    }

    fun addData(dataList: ArrayList<EvaluationData?>) {
        if(dataList.isEmpty())
            return
        dataList.add(null)
        _evaluationList.value!!.addAll(dataList)
        _evaluationList.value = _evaluationList.value //대입을 해줘야지만 옵저버가 변화를 감지함.
    }

    fun deleteLoading() {
        if(_evaluationList.value?.isEmpty() == true)
            return
        if(_evaluationList.value?.get(evaluationList.value?.size!! - 1) == null) {
            _evaluationList.value?.removeAt(evaluationList.value?.size!! - 1)
            _evaluationList.value = _evaluationList.value
        }
    }
}
