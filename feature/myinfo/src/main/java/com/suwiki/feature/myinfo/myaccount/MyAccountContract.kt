package com.suwiki.feature.myinfo.myaccount

data class MyAccountState(
  val isLoading: Boolean = false,
  val userId: String = "",
  val userEmail: String = "",
)

sealed interface MyAccountSideEffect {
  data object PopBackStack : MyAccountSideEffect
  data object NavigateChangePassword : MyAccountSideEffect
  data object NavigateWithdrawal : MyAccountSideEffect
  data class HandleException(val throwable: Throwable) : MyAccountSideEffect
}
