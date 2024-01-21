package com.suwiki.feature.lectureevaluation.editor.examevaluation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.suwiki.core.model.enums.ExamInfo
import com.suwiki.core.model.enums.ExamLevel
import com.suwiki.core.model.enums.ExamType
import com.suwiki.core.model.lectureevaluation.exam.MyExamEvaluation
import com.suwiki.core.ui.extension.decodeFromUri
import com.suwiki.domain.lectureevaluation.editor.usecase.exam.UpdateExamEvaluationUseCase
import com.suwiki.feature.lectureevaluation.editor.navigation.MyEvaluationEditRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyExamEvaluationEditViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  val updateExamEvaluationUseCase: UpdateExamEvaluationUseCase,
) : ContainerHost<MyExamEvaluationEditState, MyExamEvaluationEditSideEffect>, ViewModel() {
  override val container: Container<MyExamEvaluationEditState, MyExamEvaluationEditSideEffect> =
    container(MyExamEvaluationEditState())

  private val myExamEvaluation = savedStateHandle.get<String>(MyEvaluationEditRoute.myExamEvaluation)!!
  private val myExamEvaluationItem: MyExamEvaluation = Json.decodeFromUri(myExamEvaluation)

  suspend fun initData() = intent {
    showLoadingScreen()

    with(myExamEvaluationItem) {
      reduce {
        state.copy(
          semesterList = semesterList.toPersistentList(),
          selectedSemester = selectedSemester,
          selectedExamType = examType,
        )
      }
      examInfo.forEach { info ->
        ExamInfo.entries.forEach {
          if (it.value == info) {
            updateExamInfo(it)
          }
        }
      }
      ExamLevel.entries.forEach {
        if (it.value == examDifficulty) {
          updateExamLevel(it)
        }
      }
      updateMyExamEvaluationValue(content)
    }
    hideLoadingScreen()
  }
  fun updateExamEvaluation() = intent {
    updateExamEvaluationUseCase(
      UpdateExamEvaluationUseCase.Param(
        lectureId = myExamEvaluationItem.id,
        selectedSemester = state.selectedSemester,
        examInfo = state.examInfo.filter { it.isNotBlank() }.joinToString(", "),
        examType = state.selectedExamType,
        examDifficulty = state.examLevel!!.value,
        content = state.examEvaluation,
      ),
    )
      .onSuccess {
        popBackStack()
      }
      .onFailure {
        postSideEffect(MyExamEvaluationEditSideEffect.HandleException(it))
      }
  }

  fun updateSemester(selectedPosition: Int) = intent {
    reduce { state.copy(selectedSemester = state.semesterList[selectedPosition]) }
    hideSemesterBottomSheet()
  }

  fun updateExamType(selectedPosition: Int) = intent {
    ExamType.entries.forEach { examType ->
      if (examType.ordinal == selectedPosition) {
        reduce { state.copy(selectedExamType = examType.value) }
      }
    }
    hideExamTypeBottomSheet()
  }

  fun updateExamLevel(examLevel: ExamLevel) = intent {
    reduce { state.copy(examLevel = examLevel) }
  }

  fun updateExamInfo(info: ExamInfo) = intent {
    val examInfoList = state.examInfo.toMutableList()

    if (info.value in state.examInfo) {
      examInfoList.remove(info.value)
    } else {
      examInfoList.add(info.value)
    }
    reduce { state.copy(examInfo = examInfoList.toPersistentList()) }
  }

  @OptIn(OrbitExperimental::class)
  fun updateMyExamEvaluationValue(examEvaluationValue: String) = blockingIntent {
    reduce { state.copy(examEvaluation = examEvaluationValue) }
  }

  private fun showLoadingScreen() = intent { reduce { state.copy(isLoading = true) } }
  private fun hideLoadingScreen() = intent { reduce { state.copy(isLoading = false) } }
  fun showSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = true) } }
  fun hideSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = false) } }
  fun showExamTypeBottomSheet() = intent { reduce { state.copy(showExamTypeBottomSheet = true) } }
  fun hideExamTypeBottomSheet() = intent { reduce { state.copy(showExamTypeBottomSheet = false) } }

  fun popBackStack() = intent { postSideEffect(MyExamEvaluationEditSideEffect.PopBackStack) }
}
