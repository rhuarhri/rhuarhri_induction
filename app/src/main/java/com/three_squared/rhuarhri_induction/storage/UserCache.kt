package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.dependency_injection.Dependencies
import com.three_squared.rhuarhri_induction.storage.data.RepositoryInternal
import com.three_squared.rhuarhri_induction.storage.data.UserInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class UserCache @Inject constructor(private val realmConfig : RealmConfiguration) {

    //TODO App Presentation slide 2
    /**
     * Development problem
     * Most of the time the app uses coroutines to handle multiple threading
     * However this caused a problem when the app had to update the cache.
     * Because when an app is closed all the coroutines are cancelled even
     * when they are doing something like updating the cache.
     * To resolve this I have used a library called work manager that
     * is designed to handle the task of updating the cache.
     */

    fun add(user: User) {

        val internalRepositories = RealmList<RepositoryInternal>()

        for (repository in user.repositoryList) {
            internalRepositories.add(
                RepositoryInternal(repository.id, repository.name, repository.visibility, repository.description)
            )
        }

        val userInternal = UserInternal(id = user.id, name = user.name,
            avatarUrl = user.avatar, repositoryUrl = user.repoListURL, repositories = internalRepositories)

        val foundUser = get(user.id)

        if (foundUser == null) {
            val realm = Realm.getInstance(realmConfig)
            realm.beginTransaction()
            realm.insert(userInternal)
            realm.commitTransaction()
        }

        if (foundUser != null) {
            updateInternalUser(user.id, user)
        }
    }

    private fun updateInternalUser(id : String, user : User) {

        val internalRepositories = RealmList<RepositoryInternal>()

        for (repo in user.repositoryList) {
            internalRepositories.add(
                RepositoryInternal(repo.id, repo.name, repo.visibility, repo.description)
            )
        }

        val realm = Realm.getInstance(realmConfig)

        realm.beginTransaction()
        val foundUser = realm.where(UserInternal::class.java)
            .equalTo("id", id)
            .findFirst()

        if (foundUser != null) {
            foundUser.name = user.name
            foundUser.avatarUrl = user.avatar
            foundUser.repositoryUrl = user.repoListURL

            foundUser.repositories.clear()
            for (newRepos in internalRepositories) {
                foundUser.repositories.add(newRepos)
            }
        }

        realm.commitTransaction()
    }

    fun get(userId : String): User? {
        var foundUser : User? = null

        val realm = Realm.getInstance(realmConfig)//super.getInstance()
        realm.beginTransaction()
        val foundUserInternal = realm.where(UserInternal::class.java)
            .equalTo("id", userId)
            .findFirst()
        realm.commitTransaction()

        if (foundUserInternal != null) {
            foundUser = mapToUser(foundUserInternal)
        }

        return foundUser
    }

    suspend fun getByName(userName : String) : List<User> {
        val foundUsers = mutableListOf<User>()

        val realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAwait { transaction ->
            foundUsers.addAll(transaction.where(UserInternal::class.java).equalTo("name", userName).findAll().map {
                mapToUser(it)
            })
        }

        return foundUsers
    }

    private fun mapToUser(userInternal : UserInternal) : User {
        val repositoryList = mutableListOf<Repository>()
        for (repositoryInternal in userInternal.repositories) {
            repositoryList.add(Repository(
                repositoryInternal.id,
                repositoryInternal.name,
                repositoryInternal.visibility,
                repositoryInternal.description
            ))
        }

        return User(userInternal.id, userInternal.repositoryUrl, userInternal.name, userInternal.avatarUrl, repositoryList)
    }
}

const val UserCacheUpdaterKey = "userJson"
class UserCacheUpdater(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val moshi = Moshi.Builder().build()
        val adapter : JsonAdapter<User> = moshi.adapter(User::class.java)

        val json = inputData.getString(UserCacheUpdaterKey)
        if (json != null) {
            val newUserInfo = adapter.fromJson(json)

            if (newUserInfo != null) {

                val userCache = UserCache(Dependencies().providesRealmConfig())
                userCache.add(newUserInfo)

            }
        }

        return Result.success()
    }

}