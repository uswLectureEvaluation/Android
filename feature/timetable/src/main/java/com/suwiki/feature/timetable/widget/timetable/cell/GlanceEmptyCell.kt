package com.suwiki.feature.timetable.widget.timetable.cell

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceComposable
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.suwiki.core.designsystem.theme.Gray6A
import com.suwiki.core.designsystem.theme.GrayF6
import com.suwiki.core.designsystem.theme.White
import androidx.glance.layout.Alignment
import androidx.glance.text.TextAlign
import com.suwiki.core.designsystem.theme.GrayCB
import com.suwiki.core.designsystem.theme.GrayDA
import com.suwiki.feature.timetable.timetable.component.timetable.MINUTE60
import com.suwiki.feature.timetable.timetable.component.timetable.timetableBorderWidth
import com.suwiki.feature.timetable.timetable.component.timetable.timetableHeightPerHour
import com.suwiki.feature.timetable.widget.timetable.glanceTimetableBorderWidth
import com.suwiki.feature.timetable.widget.timetable.glanceTimetableHeightPerHour

@Composable
@GlanceComposable
internal fun GlanceEmptyCell(
  modifier: GlanceModifier = GlanceModifier,
  minute: Int = MINUTE60,
  text: String? = null,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(glanceTimetableHeightPerHour * minute / MINUTE60)
      .background(GrayF6)
      .padding(glanceTimetableBorderWidth)
  ) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(White)
        .padding(glanceTimetableBorderWidth),
      contentAlignment = Alignment.Center
    ) {
      if (text != null) {
        Text(
          text = text,
          maxLines = 1,
          style = TextStyle(
            color = ColorProvider(Gray6A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
          ),
        )
      }
    }
  }

}
