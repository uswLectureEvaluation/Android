package com.suwiki.domain.timetable.usecase

import com.suwiki.core.common.runCatchingIgnoreCancelled
import com.suwiki.core.model.timetable.Timetable
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class GetAllTimetableUseCase @Inject constructor(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(): Result<List<Timetable>> = runCatchingIgnoreCancelled {
    timetableRepository.getAllTimetable()
  }
}
