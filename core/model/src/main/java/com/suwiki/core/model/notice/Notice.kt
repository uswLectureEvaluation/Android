package com.suwiki.core.model.notice

import java.time.LocalDateTime

data class Notice(
  val id: Long = 0,
  val title: String = "",
  val date: LocalDateTime? = null,
)
