package com.suwiki.feature.myinfo.myevaluation.lectureevaluation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.suwiki.core.model.enums.GradeLevel
import com.suwiki.core.model.enums.HomeworkLevel
import com.suwiki.core.model.enums.TeamLevel
import com.suwiki.core.model.lectureevaluation.lecture.MyLectureEvaluation
import com.suwiki.core.model.user.User
import com.suwiki.domain.user.usecase.GetUserInfoUseCase
import com.suwiki.feature.myinfo.navigation.MyInfoRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyLectureEvaluationEditViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  val getUserInfoUseCase: GetUserInfoUseCase,
) : ContainerHost<MyLectureEvaluationEditState, MyLectureEvaluationEditSideEffect>, ViewModel() {
  override val container: Container<MyLectureEvaluationEditState, MyLectureEvaluationEditSideEffect> =
    container(MyLectureEvaluationEditState())

  private val myLectureEvaluation = savedStateHandle.get<String>(MyInfoRoute.myLectureEvaluation)!!
  private val myLectureEvaluationItem: MyLectureEvaluation = Json.decodeFromString(myLectureEvaluation)

  suspend fun setInitData() {
    showLoadingScreen()
    /* TODO 에러 처리 */
    intent { reduce { state.copy(selectedSemester = myLectureEvaluationItem.selectedSemester) } }
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
    // TODO("내 강의평가 삭제 API 호출")
    popBackStack()
  }

  fun clickSemesterItem(semester: String) {
    intent { reduce { state.copy(selectedSemester = semester) } }
    hideSemesterBottomSheet()
  }

  fun updateHoneyRating(honeyRating: Float) = intent {
    reduce { state.copy(honeyRating = if (honeyRating < 0.5) 0.5F else honeyRating) }
  }

  fun updateLearningRating(learningRating: Float) = intent {
    reduce { state.copy(learningRating = if (learningRating < 0.5) 0.5F else learningRating) }
  }

  fun updateSatisfactionRating(satisfactionRating: Float) = intent {
    reduce { state.copy(satisfactionRating = if (satisfactionRating < 0.5) 0.5F else satisfactionRating) }
  }

  fun updateMyLectureEvaluationValue(lectureEvaluationValue: String) = intent {
    reduce { state.copy(lectureEvaluation = lectureEvaluationValue) }
  }

  private fun setPoint(user: User) = intent { reduce { state.copy(point = user.point) } }
  fun updateGradeLevel(gradeLevel: GradeLevel) = intent { reduce { state.copy(gradeLevel = gradeLevel) } }
  fun updateHomeworkLevel(homeworkLevel: HomeworkLevel) = intent { reduce { state.copy(homeworkLevel = homeworkLevel) } }
  fun updateTeamLevel(teamLevel: TeamLevel) = intent { reduce { state.copy(teamLevel = teamLevel) } }

  private fun showLoadingScreen() = intent { reduce { state.copy(isLoading = true) } }
  private fun hideLoadingScreen() = intent { reduce { state.copy(isLoading = false) } }
  fun showMyLectureEvaluationDeleteDialog() = intent { reduce { state.copy(showDeleteLectureEvaluationDialog = true) } }
  fun hideMyLectureEvaluationDeleteDialog() = intent { reduce { state.copy(showDeleteLectureEvaluationDialog = false) } }
  fun showSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = true) } }
  fun hideSemesterBottomSheet() = intent { reduce { state.copy(showSemesterBottomSheet = false) } }

  fun popBackStack() = intent { postSideEffect(MyLectureEvaluationEditSideEffect.PopBackStack) }
  private fun showDeleteToast() = intent { postSideEffect(MyLectureEvaluationEditSideEffect.ShowMyLectureEvaluationDeleteToast) }
  private fun showReviseToast() = intent { postSideEffect(MyLectureEvaluationEditSideEffect.ShowMyLectureEvaluationReviseToast) }
}