package com.suwiki.domain.timetable.usecase

import com.suwiki.core.common.runCatchingIgnoreCancelled
import com.suwiki.core.model.timetable.Timetable
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableUseCase @Inject constructor(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(timetable: Timetable): Result<Unit> = runCatchingIgnoreCancelled {
    with(timetableRepository) {
      deleteTimetable(timetable)
      val firstTimetableCreateTime = timetableRepository.getAllTimetable().firstOrNull()?.createTime ?: 0L
      timetableRepository.setMainTimetableCreateTime(firstTimetableCreateTime)
    }
  }
}
