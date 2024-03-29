package com.suwiki.remote.lectureevaluation.viewerreporter.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportLectureRequest(
  val evaluateIdx: Long,
  val content: String = "",
)
