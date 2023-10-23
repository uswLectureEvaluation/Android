package com.suwiki.domain.openmajor.usecase

import com.suwiki.domain.openmajor.repository.OpenMajorRepository
import javax.inject.Inject

class RegisterBookmarkUseCase @Inject constructor(
  private val openMajorRepository: OpenMajorRepository,
) {
  suspend operator fun invoke(request: String): Result<Unit> = kotlin.runCatching {
    openMajorRepository.bookmarkMajor(
      request,
    )
  }
}