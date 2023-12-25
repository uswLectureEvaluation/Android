package com.suwiki.core.network.retrofit

import kotlinx.serialization.json.Json

object Json {
  private val instance = Json { ignoreUnknownKeys = true }

  fun getSuwikiErrorBody(body: String) = instance.decodeFromString<SuwikiErrorResponse>(body)
}
