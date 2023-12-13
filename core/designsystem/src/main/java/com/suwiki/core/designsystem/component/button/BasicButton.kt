package com.suwiki.core.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.suwiki.core.ui.extension.suwikiClickable

@Composable
fun BasicButton(
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  enabled: Boolean = true,
  onClick: () -> Unit,
  enabledBackGroundColor: Color,
  pressedBackgroundColor: Color,
  disabledBackgroundColor: Color,
  interactionSource: MutableInteractionSource =
    remember { MutableInteractionSource() },
  content: @Composable (isPressed: Boolean) -> Unit,
) {
  val isPressed by interactionSource.collectIsPressedAsState()

  val btnColor = when {
    !enabled -> disabledBackgroundColor
    isPressed -> pressedBackgroundColor
    else -> enabledBackGroundColor
  }
  Box(
    modifier = modifier
      .background(
        color = btnColor,
        shape = shape,
      )
      .suwikiClickable(
        rippleEnabled = true,
        rippleColor = pressedBackgroundColor,
        onClick = onClick,
      ),
    contentAlignment = Alignment.Center,
  ) {
    content(isPressed)
  }
}

@Stable
private val BasicButtonLargeHeight = 50.dp

@Composable
fun BasicContainedButtonLarge(
  text: String,
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  enabled: Boolean = true,
  onClick: () -> Unit,
  enabledBackGroundColor: Color,
  pressedBackgroundColor: Color,
  disabledBackgroundColor: Color,
  textColor: Color,
  disabledTextColor: Color,
) {
  BasicButton(
    modifier = modifier
      .fillMaxWidth()
      .height(BasicButtonLargeHeight),
    shape = shape,
    enabled = enabled,
    onClick = onClick,
    enabledBackGroundColor = enabledBackGroundColor,
    pressedBackgroundColor = pressedBackgroundColor,
    disabledBackgroundColor = disabledBackgroundColor,
    content = {
      Text(
        text = text,
        color = if (enabled) textColor else disabledTextColor,
      )
    },
  )
}

@Stable
private val BasicContainedButtonRegularHeight = 40.dp

@Composable
fun BasicContainedButton(
  text: String,
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  enabled: Boolean = true,
  onClick: () -> Unit,
  enabledBackGroundColor: Color,
  textColor: Color,
  padding: PaddingValues = PaddingValues(0.dp, 0.dp),
) {
  BasicButton(
    modifier = modifier
      .wrapContentWidth()
      .wrapContentHeight(),
    shape = shape,
    enabled = enabled,
    onClick = onClick,
    enabledBackGroundColor = enabledBackGroundColor,
    pressedBackgroundColor = enabledBackGroundColor,
    disabledBackgroundColor = enabledBackGroundColor,
    content = {
      Text(
        text = text,
        color = textColor,
        modifier = Modifier.padding(padding),
      )
    },
  )
}

@Composable
fun BasicContainedIconButtonRegular(
  text: String,
  modifier: Modifier = Modifier,
  contentDescription: String?,
  shape: Shape = RectangleShape,
  enabled: Boolean = true,
  onClick: () -> Unit,
  enabledBackGroundColor: Color,
  textColor: Color,
  icon: Painter,
) {
  val contentColor = textColor

  BasicButton(
    modifier = modifier
      .wrapContentWidth()
      .height(BasicContainedButtonRegularHeight),
    shape = shape,
    enabled = enabled,
    onClick = onClick,
    enabledBackGroundColor = enabledBackGroundColor,
    pressedBackgroundColor = enabledBackGroundColor,
    disabledBackgroundColor = enabledBackGroundColor,
    content = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
      ) {
        Image(
          painter = icon,
          contentDescription = contentDescription,
        )
        Text(
          text = text,
          color = contentColor,
        )
      }
    },
  )
}

@Composable
fun BasicOutlineButtonLarge(
  text: String,
  modifier: Modifier = Modifier,
  shape: Shape = RectangleShape,
  enabled: Boolean = true,
  onClick: () -> Unit,
  enabledBackGroundColor: Color,
  pressedBackgroundColor: Color,
  textColor: Color,
  borderColor: Color,
) {
  BasicButton(
    modifier = modifier
      .fillMaxWidth()
      .height(BasicButtonLargeHeight)
      .border(
        width = 1.dp,
        color = borderColor,
        shape = shape,
      ),
    shape = shape,
    enabled = enabled,
    onClick = onClick,
    enabledBackGroundColor = enabledBackGroundColor,
    pressedBackgroundColor = pressedBackgroundColor,
    disabledBackgroundColor = enabledBackGroundColor,
    content = {
      Text(
        text = text,
        color = textColor,
      )
    },
  )
}