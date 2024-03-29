package com.suwiki.domain.lectureevaluation.viewerreporter.repository

import com.suwiki.core.model.lectureevaluation.exam.ExamEvaluationList

interface ExamProviderRepository {
  suspend fun buyExam(lectureId: Long)

  suspend fun getExamEvaluationList(
    lectureId: Long,
    page: Int,
  ): ExamEvaluationList
}
