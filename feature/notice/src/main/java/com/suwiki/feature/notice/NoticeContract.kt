package com.suwiki.feature.notice

import com.suwiki.core.model.notice.Notice
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class NoticeState(
  val isLoading: Boolean = false,
  val noticeList: PersistentList<Notice> = persistentListOf(),
)

sealed interface NoticeSideEffect {
  data object NavigateNoticeDetail : NoticeSideEffect
  data object PopBackStack : NoticeSideEffect
}
