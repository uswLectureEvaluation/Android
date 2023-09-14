package com.suwiki.remote.api

import com.suwiki.remote.ApiResult
import com.suwiki.remote.request.user.FindIdRequest
import com.suwiki.remote.request.user.FindPasswordRequest
import com.suwiki.remote.request.user.LoginRequest
import com.suwiki.remote.request.user.QuitRequest
import com.suwiki.remote.request.user.ResetPasswordRequest
import com.suwiki.remote.response.common.SuccessCheckResponse
import com.suwiki.remote.response.user.BlacklistResponse
import com.suwiki.remote.response.user.SuspensionHistoryResponse
import com.suwiki.remote.response.user.TokenResponse
import com.suwiki.remote.response.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// TODO : v2 api로 업그레이드 필요.
interface UserApi {
    companion object {
        const val USER = "/user"
    }

    // 아이디 찾기 API
    @POST("$USER/find-id")
    suspend fun findId(@Body findIdRequest: FindIdRequest): ApiResult<SuccessCheckResponse>

    // 비밀번호 찾기(임시 비밀번호 전송) API
    @POST("$USER/find-pw")
    suspend fun findPassword(@Body findPasswordRequest: FindPasswordRequest): ApiResult<SuccessCheckResponse>

    // 로그인 요청 API
    @POST("$USER/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResult<TokenResponse>

    // 비밀번호 재설정 API
    @POST("$USER/reset-pw")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ApiResult<SuccessCheckResponse>

    // 회원탈퇴 요청 API
    @POST("$USER/quit")
    suspend fun quit(@Body quitRequest: QuitRequest): ApiResult<SuccessCheckResponse>

    // 내 정보 페이지 호출 API
    @GET("$USER/my-page")
    suspend fun getUserData(): ApiResult<UserResponse>
}
