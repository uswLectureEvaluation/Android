package com.suwiki.feature.myinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suwiki.core.designsystem.component.bottomsheet.SuwikiBottomSheetItem
import com.suwiki.core.designsystem.shadow.cardShadow
import com.suwiki.core.designsystem.theme.Black
import com.suwiki.core.designsystem.theme.Gray6A
import com.suwiki.core.designsystem.theme.Gray95
import com.suwiki.core.designsystem.theme.GrayF6
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import com.suwiki.core.ui.extension.suwikiClickable
import okhttp3.internal.immutableListOf

@Composable
fun MyInfoRoute(
  padding: PaddingValues,
//  viewModel: MyInfoViewModel = hiltViewModel(),
  navigateNotice: () -> Unit,
) {
//  val uiState = viewModel.collectAsState().value
//  viewModel.collectSideEffect { sideEffect ->
//    when (sideEffect) {
//      MyInfoSideEffect.NavigateNotice -> navigateNotice()
//    }
//  }
//
//  LaunchedEffect(key1 = viewModel) {
//    viewModel.checkLoggedIn()
//  }
//
//  MyInfoScreen(
//    padding = padding,
//    uiState = uiState,
//    onClickNoticeButton = { viewModel.navigateNotice() },
//  )
  MyInfoScreen(
    padding = padding,
    uiState = MyInfoState(),
    onClickNoticeButton = { },
  )
}

@Composable
fun MyInfoScreen(
  padding: PaddingValues,
  uiState: MyInfoState,
  onClickNoticeButton: () -> Unit,
) {
  val loginList = immutableListOf(
    R.string.my_info_point,
    R.string.my_info_ban_history
  )
  val serviceList = immutableListOf(
    R.string.my_info_send_feedback,
    R.string.my_info_use_terms,
    R.string.my_info_privacy_policy,
    R.string.my_info_open_source_library
  )
  Column(
    modifier = Modifier.padding(padding)
  ) {
    Box(
      modifier = Modifier.background(if (uiState.isLoggedIn) GrayF6 else White)
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (uiState.isLoggedIn) {
          LoginMyInfo()
        } else {
          LogoutMyInfo()
        }
        Row(
          horizontalArrangement = Arrangement.spacedBy(24.dp),
          modifier = Modifier
            .padding(vertical = 28.dp, horizontal = 41.dp)
            .wrapContentWidth()
            .height(49.dp)
        ) {
          MyInfoMenuItem(
            title = stringResource(R.string.my_info_notice),
            iconId = R.drawable.ic_my_info_notice,
            onClickItem = onClickNoticeButton,
          )
          VerticalDivider(
            modifier = Modifier
              .width(1.dp)
              .padding(vertical = 5.dp)
          )
          MyInfoMenuItem(
            title = stringResource(R.string.my_info_contact),
            iconId = R.drawable.ic_my_info_comment
          )
          VerticalDivider(
            modifier = Modifier
              .width(1.dp)
              .padding(vertical = 5.dp),
          )
          MyInfoMenuItem(
            title = stringResource(R.string.my_info_account_manage),
            iconId = R.drawable.ic_my_info_setting
          )
        }
      }
    }
    Column(
      modifier = Modifier
        .background(White)
        .fillMaxSize()
    ) {
      if (uiState.isLoggedIn) {
        LazyColumn {
          item {
            SuwikiBottomSheetItem(title = stringResource(R.string.my_info_my))
          }
          items(items = loginList) { title ->
            MyInfoListItem(title = stringResource(title))
          }
        }
      }
      LazyColumn {
        item {
          SuwikiBottomSheetItem(title = stringResource(R.string.my_info_service))
        }
        items(items = serviceList) { title ->
          MyInfoListItem(title = stringResource(title))
        }
      }
    }
  }
}

@Composable
fun LogoutMyInfo(
  onClickLogin: () -> Unit = {},
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 52.dp, start = 24.dp, end = 24.dp)
      .background(
        color = GrayF6,
        shape = RoundedCornerShape(10.dp),
      )
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(top = 19.dp, start = 16.dp)
        .suwikiClickable(onClick = onClickLogin),
    ) {
      Text(
        text = stringResource(R.string.my_info_login),
        style = SuwikiTheme.typography.header2,
        color = Black
      )
      Image(
        painter = painterResource(id = R.drawable.ic_arrow_gray_right),
        contentDescription = "",
      )
    }
    Text(
      modifier = Modifier.padding(start = 16.dp, bottom = 19.dp),
      text = stringResource(R.string.my_info_check_mine),
      style = SuwikiTheme.typography.body4,
      color = Gray95,
    )
  }
}

@Composable
fun LoginMyInfo(
  onClickMyPost: () -> Unit = {},
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .fillMaxWidth()
      .padding(top = 52.dp, start = 24.dp, end = 24.dp)
      .cardShadow()
      .background(
        color = White,
        shape = RoundedCornerShape(10.dp),
      )
  ) {
    Column {
      Text(
        modifier = Modifier.padding(top = 19.dp, start = 16.dp),
        text = stringResource(R.string.my_info_test_nickname),
        style = SuwikiTheme.typography.header2,
        color = Black,
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, bottom = 19.dp),
      ) {
        Image(
          modifier = Modifier
            .padding(end = 4.dp),
          painter = painterResource(id = R.drawable.ic_my_info_point),
          contentDescription = "",
        )
        Text(
          text = stringResource(R.string.my_info_test_point),
          style = SuwikiTheme.typography.body4,
          color = Black,
        )
      }
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .padding(top = 31.dp, end = 6.dp)
        .suwikiClickable(onClick = onClickMyPost),
    ) {
      Text(
        text = stringResource(R.string.my_info_my_post),
        style = SuwikiTheme.typography.caption1,
        color = Black,
      )
      Image(
        painter = painterResource(id = R.drawable.ic_arrow_gray_right),
        contentDescription = "",
      )
    }
  }
}

@Composable
private fun MyInfoMenuItem(
  title: String = "",
  iconId: Int,
  onClickItem: () -> Unit = {},
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .suwikiClickable(onClick = onClickItem),
  ) {
    Image(
      painter = painterResource(id = iconId),
      contentDescription = "",
    )
    Text(
      text = title,
      style = SuwikiTheme.typography.caption2,
    )
  }
}

@Composable
private fun MyInfoListItem(
  title: String = "",
  onClick: () -> Unit = {},
) {
  Box(
    modifier = Modifier
      .background(White)
      .fillMaxWidth()
      .wrapContentHeight()
      .suwikiClickable(
        onClick = onClick,
        rippleColor = Gray6A,
      ),
  ) {
    Text(
      text = title,
      style = SuwikiTheme.typography.body2,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(top = 13.dp, bottom = 14.dp, start = 24.dp, end = 52.dp),
    )
  }
}

@Preview(showSystemUi = true)
@Composable
fun MyInfoScreenScreenPreview() {
  SuwikiTheme {
//    MyInfoScreen(
//      padding = PaddingValues(0.dp),
//      uiState = MyInfoState(),
//      onClickNoticeButton = {},
//    )
    MyInfoRoute(
      padding = PaddingValues(0.dp),
      navigateNotice = {},
    )
  }
}
