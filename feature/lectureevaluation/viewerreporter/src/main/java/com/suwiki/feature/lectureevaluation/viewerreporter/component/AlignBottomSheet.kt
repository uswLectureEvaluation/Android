package com.suwiki.feature.lectureevaluation.viewerreporter.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suwiki.core.designsystem.component.align.SuwikiAlignContainer
import com.suwiki.core.designsystem.component.bottomsheet.SuwikiBottomSheet
import com.suwiki.core.designsystem.theme.Gray95
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.feature.lectureevaluation.viewerreporter.LectureEvaluationState
import com.suwiki.feature.lectureevaluation.viewerreporter.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlignBottomSheet(
  showFilterSelectionBottomSheet: Boolean,
  hideAlignBottomSheet: () -> Unit,
  onClickAlignBottomSheetItem: (String) -> Unit,
) {
  var selectedItem by remember { mutableStateOf(-1) }
  SuwikiBottomSheet(
    sheetState = rememberModalBottomSheetState(
      skipPartiallyExpanded = true,
    ),
    isSheetOpen = showFilterSelectionBottomSheet,
    onDismissRequest = hideAlignBottomSheet,
  ) {
    AlignBottomSheetContent(
      selectedItem = selectedItem,
      onClickAlignBottomSheetItem = onClickAlignBottomSheetItem,
      bottomSheetTitle = stringResource(R.string.word_sort),
    )
  }
}

@Composable
private fun AlignBottomSheetContent(
  selectedItem: Int,
  onClickAlignBottomSheetItem: (String) -> Unit = {},
  bottomSheetTitle: String,
) {
  val filterDataList: PersistentList<String> = persistentListOf(
    "최근 올라온 강의",
    "꿀 강의",
    "만족도 높은 강의",
    "배울게 많은 강의",
    "BEST 강의",
  )
  Column(
    modifier = Modifier
      .padding(top = 36.dp, bottom = 45.dp),
  ) {
    Text(
      text = bottomSheetTitle,
      style = SuwikiTheme.typography.body5,
      color = Gray95,
      modifier = Modifier.padding(horizontal = 24.dp, vertical = 15.dp),
    )
    filterDataList.forEachIndexed { index, item ->
      val isChecked = selectedItem == index
      SuwikiAlignContainer(
        text = item,
        isChecked = isChecked,
        onClick = {
          onClickAlignBottomSheetItem(item)
        },
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun FilterSelectionBottomSheetContentPreview() {
  SuwikiTheme {
    AlignBottomSheetContent(bottomSheetTitle = stringResource(R.string.word_sort), selectedItem = -1)
  }
}
