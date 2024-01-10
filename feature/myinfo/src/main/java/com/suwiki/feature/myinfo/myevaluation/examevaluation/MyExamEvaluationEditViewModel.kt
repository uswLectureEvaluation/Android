package com.suwiki.feature.myinfo.myevaluation.examevaluation

import androidx.lifecycle.ViewModel
import com.suwiki.core.model.enums.ExamLevel
import com.suwiki.core.model.enums.ExamType
import com.suwiki.core.model.user.User
import com.suwiki.domain.user.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyExamEvaluationEditViewModel @Inject constructor(
  val getUserInfoUseCase: GetUserInfoUseCase,
) : ContainerHost<MyExamEvaluationEditState, MyExamEvaluationEditSideEffect>, ViewModel() {
  override val container: Container<MyExamEvaluationEditState, MyExamEvaluationEditSideEffect> =
    container(MyExamEvaluationEditState())

  suspend fun loadMyPoint() {
    showLoadingScreen()
    /* TODO 에러 처리 */
    getUserInfoUseCase().collect(::setPoint)
    hideLoadingScreen()
  }

  fun clickReviseButton() {
    showReviseToast()
    // TODO("내 강의평가 수정 API 호출")
    popBackStack()
  }

  fun clickDeleteButton() {
    showDeleteToast()
    // TODO("내 강의평가 수정 API 호출")
    popBackStack()
  }

  fun clickSemesterItem(semester: String) {
    intent { reduce { state.copy(selectedSemester = semester) } }
    hideSemesterBottomSheet()
  }

  fun clickExamTypeItem(examType: String) {
    intent { reduce { state.copy(selectedExamType = examType) } }
    hideExamTypeBottomSheet()
  }

  private fun setPoint(user: User) = intent { reduce { state.copy(point = user.point) } }
  fun updateExamLevel(examLevel: ExamLevel) = intent { reduce { state.copy(examLevel = examLevel) } }
  fun updateExamType(examType: ExamType) = intent { reduce { state.copy(examType = examType) } }

  fun updateMyExamEvaluationValue(examEvaluationValue: String) = intent {
    reduce { state.copy(examEvaluation = examEvaluationValue) }
  }

  private fun showLoadingScreen() = intent { reduce { state.copy(isLoading = true) } }
  private fun hideLoadingScreen() = intent { reduce { state.copy(isLoading = false) } }
  fun showMyExamEvaluationDeleteDialog() = intent { reduce { state.copy(showDeleteExamEvaluationDialog = true) } }
  fun hideMyExamEvaluationDeleteDialog() = intent { reduce { state.copy(showDeleteExamEvaluationDialog = false) } }
  fun showSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = true) } }
  fun hideSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = false) } }
  fun showExamTypeBottomSheet() = intent { reduce { state.copy(showExamTypeBottomSheet = true) } }
  fun hideExamTypeBottomSheet() = intent { reduce { state.copy(showExamTypeBottomSheet = false) } }

  fun popBackStack() = intent { postSideEffect(MyExamEvaluationEditSideEffect.PopBackStack) }
  private fun showDeleteToast() = intent { postSideEffect(MyExamEvaluationEditSideEffect.ShowMyExamEvaluationDeleteToast) }
  private fun showReviseToast() = intent { postSideEffect(MyExamEvaluationEditSideEffect.ShowMyExamEvaluationReviseToast) }
}
