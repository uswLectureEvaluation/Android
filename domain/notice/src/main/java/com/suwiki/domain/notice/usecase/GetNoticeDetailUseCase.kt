package com.suwiki.domain.notice.usecase

import com.suwiki.core.common.runCatchingIgnoreCancelled
import com.suwiki.core.model.notice.NoticeDetail
import com.suwiki.domain.notice.repository.NoticeRepository
import javax.inject.Inject

class GetNoticeDetailUseCase @Inject constructor(
  private val noticeRepository: NoticeRepository,
) {
  suspend operator fun invoke(id: Long): Result<NoticeDetail> = runCatchingIgnoreCancelled {
    noticeRepository.getNoticeDetail(id)
  }
}
