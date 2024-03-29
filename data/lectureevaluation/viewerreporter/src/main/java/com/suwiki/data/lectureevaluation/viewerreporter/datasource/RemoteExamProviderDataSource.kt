package com.suwiki.data.lectureevaluation.viewerreporter.datasource

import com.suwiki.core.model.lectureevaluation.exam.ExamEvaluationList

interface RemoteExamProviderDataSource {
  suspend fun buyExam(lectureId: Long)

  suspend fun getExamEvaluationList(
    lectureId: Long,
    page: Int,
  ): ExamEvaluationList
}
