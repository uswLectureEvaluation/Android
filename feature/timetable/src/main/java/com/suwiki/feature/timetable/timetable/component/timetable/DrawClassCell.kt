package com.suwiki.feature.timetable.timetable.component.timetable

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.suwiki.core.designsystem.theme.GrayF6
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import com.suwiki.core.model.timetable.TimetableCell
import com.suwiki.core.model.timetable.TimetableCellColor
import com.suwiki.core.ui.extension.suwikiClickable
import com.suwiki.core.ui.util.timetableCellColorHexMap
import com.suwiki.feature.timetable.R
import com.suwiki.feature.timetable.timetable.component.timetable.cell.TimetableCellType

fun TimetableCellColor.toHex(): Long = timetableCellColorHexMap[this]!!

@Composable
internal fun DrawClassCell(
  modifier: Modifier = Modifier,
  width: Dp,
  type: TimetableCellType = TimetableCellType.CLASSNAME_PROFESSOR_LOCATION,
  data: TimetableCell,
  onClick: (TimetableCell) -> Unit = { _ -> },
) {
  val (showProfessor, showLocation) = when (type) {
    TimetableCellType.CLASSNAME -> (false to false)
    TimetableCellType.CLASSNAME_LOCATION -> (false to true)
    TimetableCellType.CLASSNAME_PROFESSOR -> (true to false)
    TimetableCellType.CLASSNAME_PROFESSOR_LOCATION -> (true to true)
  }

  val height = (data.endPeriod - data.startPeriod + 1) * timetableHeightPerHour - 8.dp

  Column(
    modifier = modifier
      .size(
        width = width,
        height = height,
      )
      .offset(
        x = (width) * (data.day.ordinal + 1),
        y = data.startPeriod * timetableHeightPerHour + timetableHeightPerHour * 0.5f,
      )
      .border(1.dp, GrayF6)
      .padding(1.dp)
      .background(Color(data.color.toHex()))
      .suwikiClickable {
        onClick(data)
      },
  ) {
    Text(
      style = SuwikiTheme.typography.caption3,
      text = data.name,
      color = White,
    )

    if (showProfessor) {
      Text(
        style = SuwikiTheme.typography.caption6,
        text = data.professor,
        color = White,
      )
    }

    if (showLocation) {
      Text(
        style = SuwikiTheme.typography.caption6,
        text = data.location,
        color = White,
      )
    }
  }
}

@Preview
@Composable
fun TimetableClassCellPreview() {
  SuwikiTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      DrawClassCell(
        width = 50.dp,
        type = TimetableCellType.CLASSNAME,
        data = TimetableCell(
          name = "도전과 창조",
          professor = "김수미",
          location = "미래혁신관B202",
          startPeriod = 1,
          endPeriod = 3,
          color = TimetableCellColor.GREEN,
        ),
      )

      DrawClassCell(
        width = 50.dp,
        type = TimetableCellType.CLASSNAME_PROFESSOR,
        data = TimetableCell(
          name = "도전과 창조",
          professor = "김수미",
          location = "미래혁신관B202",
          startPeriod = 1,
          endPeriod = 3,
          color = TimetableCellColor.GREEN,
        ),
      )

      DrawClassCell(
        width = 50.dp,
        type = TimetableCellType.CLASSNAME_LOCATION,
        data = TimetableCell(
          name = "도전과 창조",
          professor = "김수미",
          location = "미래혁신관B202",
          startPeriod = 1,
          endPeriod = 3,
          color = TimetableCellColor.GREEN,
        ),
      )

      DrawClassCell(
        width = 50.dp,
        type = TimetableCellType.CLASSNAME_PROFESSOR_LOCATION,
        data = TimetableCell(
          name = "도전과 창조",
          professor = "김수미",
          location = "미래혁신관B202",
          startPeriod = 1,
          endPeriod = 3,
          color = TimetableCellColor.GREEN,
        ),
      )
    }
  }
}
