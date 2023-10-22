package com.suwiki.core.model.notice

import java.time.LocalDateTime

data class NoticeDetail(
    val id: Long,
    val title: String,
    val date: LocalDateTime?,
    val content: String,
)