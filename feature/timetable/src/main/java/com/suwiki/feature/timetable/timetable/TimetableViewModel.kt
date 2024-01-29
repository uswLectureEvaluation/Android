package com.suwiki.feature.timetable.timetable

import androidx.lifecycle.ViewModel
import com.suwiki.core.model.timetable.TimetableCell
import com.suwiki.domain.timetable.usecase.DeleteTimetableCellUseCase
import com.suwiki.domain.timetable.usecase.GetMainTimetableUseCase
import com.suwiki.domain.timetable.usecase.GetTimetableCellTypeUseCase
import com.suwiki.domain.timetable.usecase.SetTimetableCellTypeUseCase
import com.suwiki.core.ui.argument.toCellEditorArgument
import com.suwiki.feature.timetable.timetable.component.timetable.cell.TimetableCellType
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
  private val getMainTimetableUseCase: GetMainTimetableUseCase,
  private val deleteTimetableCellUseCase: DeleteTimetableCellUseCase,
  private val getTimetableCellTypeUseCase: GetTimetableCellTypeUseCase,
  private val setTimetableCellTypeUseCase: SetTimetableCellTypeUseCase,
) : ViewModel(), ContainerHost<TimetableState, TimetableSideEffect> {
  override val container: Container<TimetableState, TimetableSideEffect> = container(TimetableState())

  fun getMainTimetable() = intent {
    val cellType = TimetableCellType.getType(getTimetableCellTypeUseCase().getOrNull())

    getMainTimetableUseCase()
      .onSuccess { timetable ->
        reduce {
          state.copy(
            timetable = timetable,
            cellType = cellType,
            showTimetableEmptyColumn = timetable == null,
          )
        }
      }
      .onFailure {
        postSideEffect(TimetableSideEffect.HandleException(it))
      }
  }

  fun deleteCell(cell: TimetableCell) = intent {
    deleteTimetableCellUseCase(cell)
      .onSuccess {
        reduce {
          state.copy(
            showEditCellBottomSheet = false,
            timetable = it,
          )
        }
      }
      .onFailure { postSideEffect(TimetableSideEffect.HandleException(it)) }
  }

  fun showEditCellBottomSheet(cell: TimetableCell) = intent {
    reduce {
      state.copy(
        showEditCellBottomSheet = true,
        selectedCell = cell,
      )
    }
  }

  fun updateCellType(position: Int) = intent {
    val cellType = TimetableCellType.entries[position]
    setTimetableCellTypeUseCase(cellType.name)
      .onSuccess { reduce { state.copy(cellType = cellType) } }
  }

  fun showSelectCellTypeBottomSheet() = intent { reduce { state.copy(showSelectCellTypeBottomSheet = true) } }
  fun hideSelectCellTypeBottomSheet() = intent { reduce { state.copy(showSelectCellTypeBottomSheet = false) } }

  fun navigateCellEdit(cell: TimetableCell) = intent { postSideEffect(TimetableSideEffect.NavigateCellEditor(cell.toCellEditorArgument())) }

  fun hideEditCellBottomSheet() = intent { reduce { state.copy(showEditCellBottomSheet = false) } }

  fun navigateTimetableEditor() = intent { postSideEffect(TimetableSideEffect.NavigateTimetableEditor) }
  fun navigateTimetableList() = intent { postSideEffect(TimetableSideEffect.NavigateTimetableList) }

  fun navigateAddTimetableCell() = intent {
    if (state.timetable == null) {
      postSideEffect(TimetableSideEffect.ShowNeedCreateTimetableToast)
    } else {
      postSideEffect(TimetableSideEffect.NavigateAddTimetableCell)
    }
  }
}
