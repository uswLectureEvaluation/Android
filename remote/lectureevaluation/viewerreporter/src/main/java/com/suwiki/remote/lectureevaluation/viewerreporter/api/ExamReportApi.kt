package com.suwiki.remote.lectureevaluation.viewerreporter.api

import com.suwiki.core.network.retrofit.ApiResult
import com.suwiki.remote.lectureevaluation.viewerreporter.api.LectureReportApi.Companion.REPORT
import com.suwiki.remote.lectureevaluation.viewerreporter.api.LectureReportApi.Companion.USER
import com.suwiki.remote.lectureevaluation.viewerreporter.request.ReportExamRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ExamReportApi {

  // 시험 정보 신고하기
  @POST("$USER/$REPORT/exam")
  suspend fun reportExam(@Body reportExamRequest: ReportExamRequest): ApiResult<Unit>
}
