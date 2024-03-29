package com.suwiki.domain.signup.repository

interface SignupRepository {
  suspend fun signUp(
    id: String,
    password: String,
    email: String,
  )

  suspend fun checkIdOverlap(loginId: String): Boolean

  suspend fun checkEmailOverlap(email: String): Boolean
}
