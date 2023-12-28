package com.suwiki.feature.myinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suwiki.domain.user.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
  private val getUserInfoUseCase: GetUserInfoUseCase,
) : ContainerHost<MyInfoState, MyInfoSideEffect>, ViewModel() {
  override val container: Container<MyInfoState, MyInfoSideEffect> = container(MyInfoState())

  private var isLoggedIn: Boolean = false

  suspend fun checkLoggedIn() {
    viewModelScope.launch {
      isLoggedIn = getUserInfoUseCase().catch { }.lastOrNull()?.isLoggedIn == true

      if (isLoggedIn) {
        showMyService()
      } else {
        hideMyService()
      }
    }
  }

  private fun showMyService() = intent { reduce { state.copy(isLoggedIn = true) } }
  private fun hideMyService() = intent { reduce { state.copy(isLoggedIn = false) } }

  fun navigateNotice() = intent { postSideEffect(MyInfoSideEffect.NavigateNotice) }
}
