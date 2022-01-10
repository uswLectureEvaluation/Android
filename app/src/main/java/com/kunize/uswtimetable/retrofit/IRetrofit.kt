package com.kunize.uswtimetable.retrofit

import com.google.gson.JsonElement
import com.kunize.uswtimetable.dataclass.*
import com.kunize.uswtimetable.util.API.EXAM
import com.kunize.uswtimetable.util.API.LECTURE
import com.kunize.uswtimetable.util.API.MY_PAGE
import com.kunize.uswtimetable.util.API.NOTICE
import com.kunize.uswtimetable.util.API.PASSWORD
import com.kunize.uswtimetable.util.API.SIGN_UP
import com.kunize.uswtimetable.util.API.SIGN_UP_EMAIL_CHECK
import com.kunize.uswtimetable.util.API.SIGN_UP_ID_CHECK
import com.kunize.uswtimetable.util.API.SIGN_UP_SCHOOL_CHECK
import com.kunize.uswtimetable.util.Constants.TOKEN
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    // 메인 페이지 요청 API
    @GET()
    fun getMain(): Call<JsonElement>

    // 공지사항 리스트 API
    @GET(NOTICE)
    fun getNoticeList(): Call<List<NoticeDto>>

    // 공지사항 API
    @GET(NOTICE)
    fun getNotice(@Query("notice_id") noticeId: Int): Call<NoticeDto>

    // 비밀번호 찾기(임시 비밀번호 전송) API
    @GET(PASSWORD)
    fun findPassword(): Call<JsonElement>

    // 검색결과 페이지 호출 API
    @GET(LECTURE)
    fun getSearchResult(@Query("query") searchTerm: String): Call<JsonElement>

    // 검색결과 자세히 보기(강의평)
    @GET(LECTURE)
    fun getSearchResultDetail(
        @Query("subjtNmname") subjectName: String,
        @Query("reprPrfsEnoNm") professorName: String
    ): Call<JsonElement>

    // 시험 정보 보기 API
    @GET(EXAM)
    fun getExamInfo(
        @Query("subjtNmname") subjectName: String,
        @Query("reprPrfsEnoNm") professorName: String
    ): Call<JsonElement>

    // 내 정보 페이지 호출 API
    @GET(MY_PAGE)
    fun getUserInfo(@Path(TOKEN) token: String): Call<JsonElement>

    // TODO RetrofitManager에 추가
    // 회원가입 요청 API
    @POST(SIGN_UP)
    fun signUp(@Body info: SignUpFormat): Call<JsonElement>

    // 아이디 중복 확인 요청 API
    @POST(SIGN_UP_ID_CHECK)
    fun idCheck(@Body id: IdData): Call<JsonElement>

    // 이메일 중복 확인 요청 API
    @POST(SIGN_UP_EMAIL_CHECK)
    fun emailCheck(@Body email: EmailData): Call<JsonElement>

    // 학교 메일 인증 API
    @POST(SIGN_UP_SCHOOL_CHECK)
    fun schoolCheck(@Body certificationNumber: SignUpSchoolCheckFormat): Call<JsonElement>

    // TODO 나머지 API도 추가
}