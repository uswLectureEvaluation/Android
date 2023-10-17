package com.suwiki.token.storage.datasource

import com.suwiki.data.datasource.local.LocalTokenStorageDataSource
import com.suwiki.model.Token
import com.suwiki.security.SecurityPreferences
import javax.inject.Inject

class LocalTokenStorageDataSourceImpl @Inject constructor(
    private val securityPreferences: SecurityPreferences,
) : LocalTokenStorageDataSource {

    override suspend fun saveToken(token: Token) {
        securityPreferences.setAccessToken(token.accessToken)
        securityPreferences.setRefreshToken(token.refreshToken)
    }

    override suspend fun clearToken() {
        saveToken(Token("", ""))
    }
}