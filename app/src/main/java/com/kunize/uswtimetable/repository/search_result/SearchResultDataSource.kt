package com.kunize.uswtimetable.repository.search_result

import com.kunize.uswtimetable.data.remote.DataDto
import com.kunize.uswtimetable.data.remote.LectureMain
import retrofit2.Response

interface SearchResultDataSource {
    suspend fun getSearchResultDataSource(name: String, option: String, page: Int, majorType: String): Response<DataDto<MutableList<LectureMain?>>>
}