package com.kunize.uswtimetable.repository.open_major

import com.kunize.uswtimetable.data.remote.OpenMajorList
import com.kunize.uswtimetable.data.remote.OpenMajorVersion
import com.kunize.uswtimetable.retrofit.IRetrofit
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response

class OpenMajorRemoteDataSource(private val apiService: IRetrofit): OpenMajorDataSource {
    override suspend fun getOpenMajorVersion(): Response<OpenMajorVersion> {
        return apiService.getOpenMajorVersion()
    }

    override suspend fun getOpenMajorList(): Response<OpenMajorList> {
        return apiService.getOpenMajorList()
    }

    override suspend fun bookmarkMajor(majorName: String): Response<String> {
        return apiService.bookmarkMajor(majorName)
    }

    override suspend fun getBookmarkMajorList(): Response<OpenMajorList> {
        return apiService.getBookmarkMajorList()
    }

    override suspend fun clearBookmarkMajor(majorName: String): Response<String> {
        return apiService.clearBookmarkMajor(majorName)
    }
}