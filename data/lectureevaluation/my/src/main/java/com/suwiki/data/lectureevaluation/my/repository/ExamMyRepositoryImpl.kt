package com.suwiki.data.lectureevaluation.my.repository

import com.suwiki.core.model.lectureevaluation.PurchaseHistory
import com.suwiki.core.model.lectureevaluation.exam.MyExamEvaluation
import com.suwiki.data.lectureevaluation.my.datasource.RemoteExamMyDataSource
import com.suwiki.domain.lectureevaluation.my.repository.ExamMyRepository
import javax.inject.Inject

class ExamMyRepositoryImpl @Inject constructor(
  private val remoteExamMyDataSource: RemoteExamMyDataSource,
) : ExamMyRepository {
  override suspend fun getMyExamEvaluationList(page: Int): List<MyExamEvaluation> {
    return remoteExamMyDataSource.getMyExamEvaluationList(page = page)
  }

  override suspend fun getPurchaseHistory(): List<PurchaseHistory> {
    return remoteExamMyDataSource.getPurchaseHistory()
  }
}
