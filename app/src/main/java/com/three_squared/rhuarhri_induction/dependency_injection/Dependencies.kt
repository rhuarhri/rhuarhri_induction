package com.three_squared.rhuarhri_induction.dependency_injection

import android.content.Context
import com.three_squared.rhuarhri_induction.DependencyBase
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.search_screen.SearchScreenRepository
import com.three_squared.rhuarhri_induction.storage.*
import com.three_squared.rhuarhri_induction.view_commit_screen.ViewCommitScreenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Dependencies {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app : Context) : DependencyBase {
        return app as DependencyBase
    }

    @Singleton
    @Provides
    fun provideRetroFit() : Retrofit {
        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.github.com/").build()
    }

    @Singleton
    @Provides
    fun provideConnectionChecker(@ApplicationContext app : Context) : ConnectionChecker {
        return ConnectionChecker(app)
    }

    @Singleton
    @Provides
    fun provideQueryHandler() : QueryHandler {
        return QueryHandler(provideRetroFit())
    }

    @Singleton
    @Provides
    fun provideUserCache() : UserCache {
        return UserCache(providesRealmConfig())
    }

    @Singleton
    @Provides
    fun provideRepositoryCache() : RepositoryCache {
        return RepositoryCache(providesRealmConfig())
    }

    @Singleton
    @Provides
    fun provideCommitCache() : CommitCache {
        return CommitCache(providesRealmConfig())
    }

    @Singleton
    @Provides
    fun provideSearchScreenRepository(@ApplicationContext app : Context) : SearchScreenRepository {
        return SearchScreenRepository(app, provideQueryHandler(),
            provideConnectionChecker(app), provideUserCache())
    }

    @Singleton
    @Provides
    fun provideViewCommitScreenRepository(@ApplicationContext app : Context) : ViewCommitScreenRepository {
        return ViewCommitScreenRepository(app, provideQueryHandler(),
            provideCommitCache(), provideConnectionChecker(app))
    }

    @Singleton
    @Provides
    fun providesRealmConfig() : RealmConfiguration {
        return RealmConfiguration.Builder()
            .name("mainDB")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
    }
}