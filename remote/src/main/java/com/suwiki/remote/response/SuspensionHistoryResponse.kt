package com.suwiki.remote.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuspensionHistoryResponse(
    @SerialName("restrictedReason") val bannedReason: String, // 정지 사유
    val judgement: String, // 조치 사항
    val createdAt: LocalDateTime, // 정지 조치 시각
    @SerialName("restrictingDate") val expiredAt: LocalDateTime, // 정지 풀리는 시각
)
