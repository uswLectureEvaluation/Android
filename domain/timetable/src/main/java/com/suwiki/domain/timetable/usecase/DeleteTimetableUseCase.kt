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

      val mainTimetableCreateTime = getMainTimetableCreateTime()
      if (mainTimetableCreateTime != timetable.createTime) return@runCatchingIgnoreCancelled

      val firstTimetableCreateTime = getAllTimetable().firstOrNull()?.createTime ?: 0L
      setMainTimetableCreateTime(firstTimetableCreateTime)
    }
  }
}
