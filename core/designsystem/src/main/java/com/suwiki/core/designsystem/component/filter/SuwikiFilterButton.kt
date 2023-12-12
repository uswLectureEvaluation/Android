package com.suwiki.core.designsystem.component.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.suwiki.core.designsystem.R
import com.suwiki.core.ui.extension.suwikiClickable

@Composable
fun SuwikiFilterButton(
  text: String,
  onClick: () -> Unit = {},
) {
  Box(
    modifier = Modifier
      .suwikiClickable(onClick = onClick)
      .wrapContentHeight()
      .padding(vertical = 4.dp, horizontal = 8.dp),
  ) {
    Row(
      modifier = Modifier
        .wrapContentHeight(),
    ) {
      Text(
        text = text,
        color = Color(0xFF346CFD),
        fontSize = 16.sp,
        modifier = Modifier
          .wrapContentHeight(),
      )
      Icon(
        painter = painterResource(id = R.drawable.ic_filter_arrow_down),
        contentDescription = "",
        tint = Color(0xFF346CFD),
        modifier = Modifier
          .size(24.dp)
          .padding(vertical = 9.dp, horizontal = 7.dp),
      )
    }
  }
}

@Preview(widthDp = 400, heightDp = 50)
@Composable
fun SuwikiFilterButtonPreview() {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SuwikiFilterButton(text = "정보통신공학과")
  }
}
