package com.suwiki.feature.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suwiki.core.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.suwiki.core.designsystem.component.loading.LoadingScreen
import com.suwiki.core.designsystem.theme.Black
import com.suwiki.core.designsystem.theme.Gray95
import com.suwiki.core.designsystem.theme.GrayF6
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NoticeDetailRoute(
  padding: PaddingValues,
  viewModel: NoticeDetailViewModel = hiltViewModel(),
  popBackStack: () -> Unit = {},
) {
  val uiState = viewModel.collectAsState().value
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      NoticeDetailSideEffect.PopBackStack -> popBackStack()
    }
  }

  LaunchedEffect(key1 = viewModel) {
    viewModel.loadNoticeDetail()
  }

  NoticeDetailScreen(
    padding = padding,
    uiState = uiState,
    title = uiState.noticeDetail.title,
    date = uiState.noticeDetail.date.toString(),
    content = uiState.noticeDetail.content,
    popBackStack = viewModel::popBackStack,
  )
}

@Composable
fun NoticeDetailScreen(
  padding: PaddingValues,
  uiState: NoticeDetailState,
  title: String,
  date: String,
  content: String,
  popBackStack: () -> Unit,
) {
  Column(
    modifier = Modifier
      .padding(padding)
      .background(White)
      .fillMaxSize(),
  ) {
    SuwikiAppBarWithTitle(
      title = "",
      onClickBack = popBackStack,
      enabledBack = true,
      enabledRemove = false,
    )

    Column {
      NoticeDetailTitleContainer(
        title = title,
        date = date,
      )
      HorizontalDivider(
        thickness = 4.dp,
        color = GrayF6,
      )
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        modifier = Modifier
          .padding(24.dp),
        text = content,
        style = SuwikiTheme.typography.body7,
      )
    }
  }
  if (uiState.isLoading) {
    LoadingScreen()
  }
}

@Composable
private fun NoticeDetailTitleContainer(
  modifier: Modifier = Modifier,
  title: String = "",
  date: String = "",
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(White)
      .padding(24.dp, 15.dp),
  ) {
    Text(
      text = title,
      style = SuwikiTheme.typography.header6,
      color = Black,
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = date,
      style = SuwikiTheme.typography.caption6,
      color = Gray95,
    )
  }
}

@Preview
@Composable
fun NoticeDetailScreenPreview() {
  SuwikiTheme {
    NoticeDetailScreen(
      padding = PaddingValues(0.dp),
      title = "2023년 04월 17일 데이터베이스 문제",
      date = "2023.04.17",
      content = "데이터 베이스의 불문명현 원인으로 인해 특정 되돌리는 풀백을 수행했습니다. \n" +
        "\n" +
        "이로 인해 회원가입을 진행해주셨으나 회원가입 처리가 \n" +
        "되어있지 않는 현상 및 \n" +
        "강의평가 시험정보 작성을 하였으나 등록되지 않는 \n" +
        "경우가 발생할 수 있습니다\n" +
        "\n" +
        "양해부탁드립니다. \n" +
        "\n" +
        "감사합니다.",
      uiState = NoticeDetailState(true),
      popBackStack = {},
    )
  }
}
