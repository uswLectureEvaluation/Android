package com.kunize.uswtimetable.repository.search_result

import com.kunize.uswtimetable.repository.evaluation.EvaluationDataSource
import com.kunize.uswtimetable.retrofit.IRetrofit

class SearchResultRemoteDataSource(private val apiService: IRetrofit) : SearchResultDataSource,
    EvaluationDataSource {
    override suspend fun getSearchResultDataSource(
        name: String,
        option: String,
        page: Int,
        majorType: String
    ) = apiService.getSearchResultDetail(name, option, page, majorType)

    override suspend fun getEvaluationDataSource(
        option: String,
        page: Int,
        majorType: String
    ) = apiService.getLectureMainList(option, page, majorType)
}