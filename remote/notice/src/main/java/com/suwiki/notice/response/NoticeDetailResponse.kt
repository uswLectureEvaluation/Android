package com.suwiki.notice.response

import com.suwiki.core.model.NoticeDetail
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeDetailResponse(
    val id: Long,
    val title: String,
    @SerialName("modifiedDate") val date: LocalDateTime?,
    val content: String,
)

internal fun NoticeDetailResponse.toModel() = com.suwiki.core.model.NoticeDetail(
    id = id,
    title = title,
    date = date?.toJavaLocalDateTime(),
    content = content,
)