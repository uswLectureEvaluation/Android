package com.kunize.uswtimetable.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.R
import com.kunize.uswtimetable.databinding.ItemRecyclerEvaluationBinding
import com.kunize.uswtimetable.databinding.ItemRecyclerProgressBinding
import com.kunize.uswtimetable.dataclass.EvaluationData
import com.kunize.uswtimetable.dataclass.LectureItemViewType

class EvaluationListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var viewType: Int = 0
    var evaluationListData = mutableListOf<EvaluationData?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = ItemRecyclerEvaluationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Holder(binding)
            }
            else -> {
                val binding = ItemRecyclerProgressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (evaluationListData[position] != null)
            1
        else
            0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = evaluationListData[position]
        if (holder is Holder && data != null)
            holder.setData(data)
    }

    override fun getItemCount(): Int = evaluationListData.size

    inner class LoadingHolder(private val binding: ItemRecyclerProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class Holder(private val binding: ItemRecyclerEvaluationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(data: EvaluationData) {
            with(binding) {
                //viewType에 따라 뷰 숨김/보임 처리
                //해당 로그가 없을 경우 가끔 적용되지 않음.
                Log.d("viewType", "$viewType")
                when (viewType) {
                    //강의평가 목록
                    LectureItemViewType.SHORT -> shortVisible()
                    //내가 쓴 강의평가
                    LectureItemViewType.USERLECTURE -> userLectureVisible()
                    //내가 쓴 시험 정보
                    LectureItemViewType.USEREXAM -> userExamVisible()
                    //세부 강의평가 정보
                    LectureItemViewType.LECTURE -> lectureVisible()
                    //세부 시험 정보
                    LectureItemViewType.EXAM -> examVisible()
                }

                evaluationData = data
                seeDetail.setOnClickListener {
                    if (detailScoreLayout.isVisible) {
                        seeDetail.setText(R.string.see_detail)
                        detailScoreLayout.visibility = View.GONE
                    } else {
                        seeDetail.setText(R.string.see_short)
                        detailScoreLayout.visibility = View.VISIBLE
                    }
                }
                executePendingBindings()
            }
        }

        private fun ItemRecyclerEvaluationBinding.shortVisible() {
            nameGroup.visibility = View.VISIBLE
            averGroup.visibility = View.VISIBLE
            lectureType.visibility = View.VISIBLE
            etcInfoGroup.visibility = View.GONE
            itemLayout.isFocusable = true
            itemLayout.isClickable = true
        }

        private fun ItemRecyclerEvaluationBinding.lectureVisible() {
            examVisible()
            averGroup.visibility = View.VISIBLE
        }

        private fun ItemRecyclerEvaluationBinding.userLectureVisible() {
            userExamVisible()
            averGroup.visibility = View.VISIBLE
        }

        private fun ItemRecyclerEvaluationBinding.userExamVisible() {
            yearSemester.visibility = View.VISIBLE
            editDeleteGroup.visibility = View.VISIBLE
            nameGroup.visibility = View.VISIBLE
            content.visibility = View.VISIBLE
        }

        private fun ItemRecyclerEvaluationBinding.examVisible() {
            yearSemester.visibility = View.VISIBLE
            reportBtn.visibility = View.VISIBLE
            content.visibility = View.VISIBLE
        }
    }
}