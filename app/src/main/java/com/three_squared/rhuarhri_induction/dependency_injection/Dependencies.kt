package com.three_squared.rhuarhri_induction.dependency_injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Dependencies {

    /*@Singleton
    @Provides
    fun provideApplication(@ApplicationContext app : Context) : DependencyBase {
        return app as DependencyBase
    }*/

    @Singleton
    @Provides
    fun providesRealmConfig() : RealmConfiguration {
        return RealmConfiguration.Builder()
            .schemaVersion(1)
            .build()
    }
}