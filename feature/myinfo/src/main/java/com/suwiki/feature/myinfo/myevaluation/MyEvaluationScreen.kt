package com.suwiki.feature.myinfo.myevaluation

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suwiki.core.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.suwiki.core.designsystem.component.container.SuwikiReviewEditContainer
import com.suwiki.core.designsystem.component.loading.LoadingScreen
import com.suwiki.core.designsystem.component.tabbar.SuwikiTabBar
import com.suwiki.core.designsystem.component.tabbar.TabTitle
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import com.suwiki.core.model.lectureevaluation.exam.MyExamEvaluation
import com.suwiki.core.model.lectureevaluation.lecture.MyLectureEvaluation
import com.suwiki.core.ui.extension.collectWithLifecycle
import com.suwiki.feature.myinfo.R
import com.suwiki.feature.myinfo.myevaluation.model.MyEvaluationTab
import com.suwiki.feature.myinfo.myevaluation.model.MyExamEvaluationsSample
import com.suwiki.feature.myinfo.myevaluation.model.MyLectureEvaluationsSample
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private val MY_EVALUATION_PAGE_COUNT = MyEvaluationTab.entries.size

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyEvaluationRoute(
  padding: PaddingValues,
  viewModel: MyEvaluationViewModel = hiltViewModel(),
  popBackStack: () -> Unit = {},
  navigateMyLectureEvaluation: (String) -> Unit = {},
  navigateMyExamEvaluation: (String) -> Unit = {},
) {
  val uiState = viewModel.collectAsState().value
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      MyEvaluationSideEffect.PopBackStack -> popBackStack()
      is MyEvaluationSideEffect.NavigateMyLectureEvaluation -> navigateMyLectureEvaluation(sideEffect.lectureEvaluation)
      is MyEvaluationSideEffect.NavigateMyExamEvaluation -> navigateMyExamEvaluation(sideEffect.examEvaluation)
    }
  }
  val pagerState = rememberPagerState(pageCount = { MY_EVALUATION_PAGE_COUNT })

  LaunchedEffect(key1 = Unit) {
    viewModel.loadInitList()
  }

  LaunchedEffect(key1 = uiState.currentPage) {
    pagerState.animateScrollToPage(uiState.currentPage)
  }

  snapshotFlow { pagerState.currentPage }.collectWithLifecycle {
    viewModel.syncPager(it)
  }

  MyEvaluationScreen(
    padding = padding,
    uiState = uiState,
    pagerState = pagerState,
    onClickTab = viewModel::syncPager,
    onClickBack = viewModel::popBackStack,
    onClickLectureEvaluationEditButton = viewModel::navigateMyLectureEvaluation,
    onClickExamEvaluationEditButton = viewModel::navigateMyExamEvaluation,
  )
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyEvaluationScreen(
  padding: PaddingValues,
  uiState: MyEvaluationState,
  pagerState: PagerState = rememberPagerState(pageCount = { MY_EVALUATION_PAGE_COUNT }),
  onClickTab: (Int) -> Unit = {},
  onClickBack: () -> Unit = {},
  onClickLectureEvaluationEditButton: (String) -> Unit = {},
  onClickExamEvaluationEditButton: (String) -> Unit = {},
) {
  Column(
    modifier = Modifier
      .padding(padding)
      .background(White)
      .fillMaxSize(),
  ) {
    SuwikiAppBarWithTitle(
      title = stringResource(R.string.my_info_my_post),
      showCloseIcon = false,
      showBackIcon = true,
      onClickBack = onClickBack,
    )
    SuwikiTabBar(
      selectedTabPosition = pagerState.currentPage,
    ) {
      MyEvaluationTab.entries.forEach { tab ->
        with(tab) {
          TabTitle(
            title = stringResource(title),
            position = position,
            selected = pagerState.currentPage == position,
            onClick = { onClickTab(position) },
          )
        }
      }
    }
    HorizontalPager(
      modifier = Modifier.weight(1f),
      state = pagerState,
    ) { page ->
      when (MyEvaluationTab.entries[page]) {
        MyEvaluationTab.LECTURE_EVALUATION -> {
          MyEvaluationLazyColumn(
//            itemList = uiState.myLectureEvaluationList,
            itemList = MyLectureEvaluationsSample,
            onClickLectureEditButton = onClickLectureEvaluationEditButton,
//            onClickEditButton = onClickLectureEvaluationEditButton,
          )
        }
        MyEvaluationTab.EXAM_INFO -> {
          MyEvaluationLazyColumn(
//            itemList = uiState.myExamEvaluationList,
            itemList = MyExamEvaluationsSample,
            onClickExamEditButton = onClickExamEvaluationEditButton,
//            onClickEditButton = onClickExamEvaluationEditButton,
          )
        }
      }
    }
  }
  if (uiState.isLoading) {
    LoadingScreen()
  }
}

@Composable
fun MyEvaluationLazyColumn(
  modifier: Modifier = Modifier,
  itemList: PersistentList<Any>,
  onClickLectureEditButton: (String) -> Unit = {},
  onClickExamEditButton: (String) -> Unit = {},
  onClickEditButton: () -> Unit = {},
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
  ) {
    items(items = itemList) { item ->
      when (item) {
        is MyLectureEvaluation -> {
          SuwikiReviewEditContainer(
            semesterText = item.selectedSemester,
            classNameText = item.lectureInfo.lectureName,
            onClickEditButton = { onClickLectureEditButton(Json.encodeToString(item)) },
          )
        }
        is MyExamEvaluation -> {
          val (examSemester, examName) = item.selectedSemester to item.lectureName

          SuwikiReviewEditContainer(
            semesterText = examSemester ?: "학기",
            classNameText = examName ?: "과목명",
            onClickEditButton = { onClickExamEditButton(Json.encodeToString(item)) },
          )
        }
        // TODO(REMOVE)
        is String -> {
          SuwikiReviewEditContainer(
            semesterText = "학기",
            classNameText = item,
            onClickEditButton = onClickEditButton,
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun MyEvaluationPreview() {
  var currentPage by remember { mutableIntStateOf(0) }
  SuwikiTheme {
    MyEvaluationScreen(
      padding = PaddingValues(0.dp),
      uiState = MyEvaluationState(
        currentPage = currentPage,
      ),
      onClickTab = { currentPage = it },
    )
  }
}
