package com.suwiki.core.designsystem.component.align

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suwiki.core.designsystem.R
import com.suwiki.core.ui.extension.suwikiClickable

@Composable
fun SuwikiAlignContainer(
  text: String,
  isChecked: Boolean = false,
  onClick: () -> Unit = {},
) {
  val textColor = if (isChecked) {
    Color(0xFF346CFD)
  } else {
    Color(0xFF6A6A6A)
  }
  Box(
    modifier = Modifier
      .background(Color(0xFFFFFFFF))
      .fillMaxWidth()
      .height(50.dp)
      .suwikiClickable(
        onClick = onClick,
        rippleColor = Color(0xFF6a6a6a),
      ),
  ) {
    Text(
      text = text,
      color = textColor,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(start = 24.dp, end = 52.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
    )
    if (isChecked) {
      Icon(
        painter = painterResource(id = R.drawable.ic_align_checked),
        contentDescription = "",
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .padding(end = 16.dp)
          .size(24.dp),
        tint = Color(0xFF346CFD),
      )
    }
  }
}

@Preview(widthDp = 300, heightDp = 200)
@Composable
fun SuwikiAlignContainerPreview() {
  var isChecked by remember { mutableStateOf(false) }

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SuwikiAlignContainer(
      text = "메뉴",
      isChecked = isChecked,
      onClick = { isChecked = !isChecked },
    )
  }
}