package com.suwiki.notice.response

import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(val data: T)
