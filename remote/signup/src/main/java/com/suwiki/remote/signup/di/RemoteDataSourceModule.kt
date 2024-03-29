package com.suwiki.remote.signup.di

import com.suwiki.data.signup.datasource.RemoteSignUpDataSource
import com.suwiki.remote.signup.datasource.RemoteSignUpDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

  @Singleton
  @Binds
  abstract fun bindRemoteSignUpDatasource(
    remoteSignUpDataSourceImpl: RemoteSignUpDataSourceImpl,
  ): RemoteSignUpDataSource
}
