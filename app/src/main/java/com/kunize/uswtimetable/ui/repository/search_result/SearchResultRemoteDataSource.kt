package com.kunize.uswtimetable.ui.repository.search_result

import com.kunize.uswtimetable.dataclass.LectureMainDto
import com.kunize.uswtimetable.retrofit.IRetrofit
import com.kunize.uswtimetable.ui.repository.evaluation.EvaluationDataSource
import retrofit2.Response

class SearchResultRemoteDataSource(private val apiService: IRetrofit) : SearchResultDataSource, EvaluationDataSource {
    override suspend fun getSearchResultDataSource(
        name: String,
        option: String,
        page: Int
    ): Response<LectureMainDto> {
        return apiService.getSearchResultDetail(name, option, page)
    }

    override suspend fun getEvaluationDataSource(
        option: String,
        page: Int
    ): Response<LectureMainDto> {
        return apiService.getLectureMainList(option, page)
    }
}