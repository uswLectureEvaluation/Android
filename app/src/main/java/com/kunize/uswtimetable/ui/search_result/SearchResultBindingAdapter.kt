package com.kunize.uswtimetable.ui.search_result

import android.annotation.SuppressLint
import android.util.Log
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.data.remote.LectureMain
import com.kunize.uswtimetable.ui.evaluation.EvaluationFooterAdapter

@SuppressLint("NotifyDataSetChanged")
@BindingAdapter("searchResultList")
fun setList(
    recyclerView: RecyclerView,
    items: MutableList<LectureMain?>
) {
    if (recyclerView.adapter == null)
        recyclerView.adapter = EvaluationFooterAdapter { }
    val evaluationAdapter = recyclerView.adapter as SearchResultAdapter

    val prevItemSize = evaluationAdapter.evaluationListData.size
    val newItemSize = items.size

    Log.d("Scroll", "$prevItemSize $newItemSize")
    evaluationAdapter.evaluationListData = items
    //api를 호출했을 때 불러올 수 있는 데이터 개수의 최대값이 11이므로 만약 newItemSize의 크기가 11보다 크다면 데이터가 추가되었음을 의미
    when {
        newItemSize == prevItemSize -> evaluationAdapter.notifyDataSetChanged()
        newItemSize > 11 -> evaluationAdapter.notifyItemRangeInserted(
            prevItemSize + 1,
            newItemSize - prevItemSize + 1
        )
        else -> evaluationAdapter.notifyDataSetChanged()
    }
}

@BindingAdapter("radioBtnClicked")
fun setCheck(radioButton: RadioButton, isChecked: Boolean) {
    radioButton.isChecked = isChecked
    if (isChecked)
        radioButton.text = radioButton.text.toString() + " ↑"
    else
        radioButton.text = radioButton.text.toString().replace(" ↑", "")
}
