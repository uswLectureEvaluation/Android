package com.kunize.uswtimetable.data.repository

import com.kunize.uswtimetable.data.datastore.UserPreference
import com.kunize.uswtimetable.dataclass.LoggedInUser
import com.kunize.uswtimetable.domain.di.AuthApiService
import com.kunize.uswtimetable.domain.repository.UserRepository
import com.kunize.uswtimetable.retrofit.IRetrofit
import com.kunize.uswtimetable.util.SuwikiApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    @AuthApiService private val apiService: IRetrofit,
    private val userPreference: UserPreference,
) : UserRepository {
    override val isLoggedIn: Flow<Boolean>
        get() = userPreference.isLoggedIn

    override val userInfo: Flow<LoggedInUser?>
        get() = combine(
            userPreference.userId,
            userPreference.point,
            userPreference.writtenEvaluation,
            userPreference.writtenExam,
            userPreference.viewExam,
            userPreference.email,
        ) {
            val userId = (it[0] as? String) ?: return@combine null
            val point = (it[1] as? Int) ?: return@combine null
            val writtenEvaluation = (it[2] as? Int) ?: return@combine null
            val writtenExam = (it[3] as? Int) ?: return@combine null
            val viewExam = (it[4] as? Int) ?: return@combine null
            val email = (it[5] as? String) ?: return@combine null
            LoggedInUser(
                userId = userId,
                point = point,
                writtenEvaluation = writtenEvaluation,
                writtenExam = writtenExam,
                viewExam = viewExam,
                email = email,
            )
        }

    override suspend fun login() {
        withContext(ioDispatcher) {
            try {
                val response = apiService.getUserData()
                if (response.isSuccessful) {
                    response.body()?.apply {
                        userPreference.login(
                            userId = userId,
                            point = point,
                            writtenEvaluation = writtenEvaluation,
                            writtenExam = writtenExam,
                            viewExam = viewExam,
                            email = email,
                        )
                    } ?: run {
                        userPreference.logout()
                    }
                } else {
                    userPreference.logout()
                }
            } catch (_: Exception) {
                userPreference.logout()
            }
        }
    }

    override suspend fun logout() {
        userPreference.logout()
        SuwikiApplication.encryptedPrefs.saveAccessToken("") // TODO DataStore 로 마이그레이션
        SuwikiApplication.encryptedPrefs.saveRefreshToken("")
    }
}
