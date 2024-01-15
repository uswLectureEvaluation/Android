package com.suwiki.domain.timetable.usecase

import com.suwiki.core.common.runCatchingIgnoreCancelled
import com.suwiki.core.model.timetable.TimetableCell
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class UpdateTimetableCellUseCase @Inject constructor(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(cell: TimetableCell): Result<Unit> = runCatchingIgnoreCancelled {
    timetableRepository.updateTimetableCell(
      cell = cell,
    )
  }
}
