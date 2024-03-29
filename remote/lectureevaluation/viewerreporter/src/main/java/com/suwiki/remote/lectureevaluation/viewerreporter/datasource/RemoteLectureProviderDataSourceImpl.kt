package com.suwiki.remote.lectureevaluation.viewerreporter.datasource

import com.suwiki.core.model.lectureevaluation.lecture.LectureEvaluationAverage
import com.suwiki.core.model.lectureevaluation.lecture.LectureEvaluationExtraAverage
import com.suwiki.core.model.lectureevaluation.lecture.LectureEvaluationList
import com.suwiki.data.lectureevaluation.viewerreporter.datasource.RemoteLectureProviderDataSource
import com.suwiki.remote.lectureevaluation.viewerreporter.api.LectureViewerApi
import com.suwiki.remote.lectureevaluation.viewerreporter.response.lecture.toModel
import javax.inject.Inject

class RemoteLectureProviderDataSourceImpl @Inject constructor(
  private val lectureApi: LectureViewerApi,
) : RemoteLectureProviderDataSource {

  override suspend fun getLectureEvaluationList(
    lectureId: Long,
    page: Int,
  ): LectureEvaluationList {
    return lectureApi.getLectureEvaluationList(lectureId = lectureId, page = page)
      .getOrThrow().toModel()
  }

  override suspend fun getLectureEvaluationAverageList(
    option: String,
    page: Int,
    majorType: String,
  ): List<LectureEvaluationAverage?> {
    return lectureApi.getLectureEvaluationAverageList(
      option = option,
      page = page,
      majorType = majorType,
    ).getOrThrow().data.map { it?.toModel() }
  }

  override suspend fun retrieveLectureEvaluationAverageList(
    search: String,
    option: String,
    page: Int,
    majorType: String,
  ): List<LectureEvaluationAverage?> {
    return lectureApi.retrieveLectureEvaluationAverageList(
      searchValue = search,
      option = option,
      page = page,
      majorType = majorType,
    ).getOrThrow().data.map { it?.toModel() }
  }

  override suspend fun getLectureEvaluationExtraAverage(lectureId: Long): LectureEvaluationExtraAverage {
    return lectureApi.getLectureEvaluationExtraAverage(lectureId).getOrThrow().data.toModel()
  }
}
