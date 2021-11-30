package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.data.RepositoryInternal
import com.three_squared.rhuarhri_induction.storage.data.UserInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class UserCache @Inject constructor(private val realmConfig : RealmConfiguration) : CacheParent<User>(realmConfig) {

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
            val realm = Realm.getInstance(realmConfig)//super.getInstance()
            realm.beginTransaction()
            realm.insert(userInternal)
            realm.commitTransaction()
        }

        /*realm.executeTransactionAwait { transaction ->
            if (foundUser == null) {
                transaction.insert(userInternal)
            }
        }*/

        if (foundUser != null) {
            updateInternalUser(user.id, user)
        }
    }

    private fun updateInternalUser(id : String, user : User) {

        val internalRepositories = RealmList<RepositoryInternal>()
        //val repoCache = RepositoryCache(realmConfig)

        for (repo in user.repositoryList) {
            //repoCache.add(repo)
            internalRepositories.add(
                RepositoryInternal(repo.id, repo.name, repo.visibility, repo.description)
            )
        }

        val realm = Realm.getInstance(realmConfig)//super.getInstance()

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

            //foundUser.repositories =
            //transaction.copyToRealmOrUpdate(internalRepositories)
        }

        realm.commitTransaction()

        /*realm.executeTransactionAwait { transaction ->

            val foundUser = transaction.where(UserInternal::class.java)
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

                //foundUser.repositories =
                //transaction.copyToRealmOrUpdate(internalRepositories)
            }
        }*/
    }

    /*private suspend fun getInternalUser(userId : String) : UserInternal? {
        var foundUser : UserInternal? = null

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->

            foundUser =  transaction.where(UserInternal::class.java)
                .equalTo("id", userId)
                .findFirst()
        }

        return foundUser
    }*/

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

        /*realm.executeTransactionAwait { transaction ->

            val foundUserInternal =  transaction.where(UserInternal::class.java)
                .equalTo("id", userId)
                .findFirst()
            if (foundUserInternal != null) {
                foundUser = mapToUser(foundUserInternal)
            }

        }*/

        return foundUser
    }

    suspend fun getByName(userName : String) : List<User> {
        val foundUsers = mutableListOf<User>()

        val realm = super.getInstance()
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

    suspend fun update(users : List<User>) {
        val expired = super.hasCacheExpired()
        if (expired) {
            super.deleteAll()
        }

        for (user in users) {
            add(user)
        }
    }

    /*suspend fun clear() {
        val realm = getInstance()
        realm.executeTransactionAwait { transaction ->
            transaction.where(UserInternal::class.java).findAll().deleteAllFromRealm()
        }
    }*/
}

/*class UserConverter() {
    private val idKey = "id"
    private val nameKey = "name"
    private val repoUrlKey = "repoURL"
    private val avatarKey = "avatar"

    val repositoryIdsKey = "repositoryIds"

    fun convertToData(user : User) : Data {
        val repositoryIds = mutableListOf<String>()

        for (repo in user.repositoryList) {
            repositoryIds.add(repo.id)
        }

        return Data.Builder()
            .putString(idKey, user.id)
            .putString(nameKey, user.name)
            .putString(repoUrlKey, user.repoListURL)
            .putString(avatarKey, user.avatar)
            .putStringArray(repositoryIdsKey, repositoryIds.toTypedArray())
            .build()
    }

    fun convertToUser(data : Data) : User {
        return User(
            data.getString(idKey) ?: "",
            data.getString(repoUrlKey) ?: "",
            data.getString(nameKey) ?: "",
            data.getString(avatarKey) ?: "",
            listOf()
        )
    }
}*/



class Test {
    fun test() {
        val moshi = Moshi.Builder().build()
        val adapter : JsonAdapter<User> = moshi.adapter(User::class.java)
    }

    /*
    val moshi = Moshi.Builder().build()
            val adapter : JsonAdapter<User> = moshi.adapter(User::class.java)

            val userJson : String = adapter.toJson(user)
            println("JSON: $userJson")

            val foundUser = adapter.fromJson(userJson)

            if (foundUser != null) {
                println("found json name is ${foundUser.name}")
            } else {
                println("found user doe not exists")
            }
     */
}

/*class UserCache2(private val realmConfig: RealmConfiguration) {
    fun add(user: User) {
        println("adding user with id of ${user.id}")
        val internalRepositories = RealmList<RepositoryInternal>()

        for (repository in user.repositoryList) {
            internalRepositories.add(
                RepositoryInternal(repository.id, repository.name, repository.visibility, repository.description)
            )
        }

        val userInternal = UserInternal(id = user.id, name = user.name,
            avatarUrl = user.avatar, repositoryUrl = user.repoListURL, repositories = internalRepositories)

        val foundUser = get(user.id)

        if (foundUser != null) {
            println("updating user")
            updateInternalUser(user.id, user)
        } else {
            println("adding user")
            val realm = Realm.getInstance(realmConfig)
            realm.beginTransaction()
            realm.insert(userInternal)
            realm.commitTransaction()
        }
    }

    fun get(userId : String): User? {
        var foundUser : User? = null

        val realm = Realm.getInstance(realmConfig)
        realm.beginTransaction()
        val foundUserInternal = realm.where(UserInternal::class.java)
            .equalTo("id", userId)
            .findFirst()
        realm.commitTransaction()

        if (foundUserInternal != null) {
            foundUser = mapToUser(foundUserInternal)
            println("found user id is ${foundUser.id}")
        } else {
            println("User does not exists")
        }

        return foundUser
    }

    private fun updateInternalUser(id : String, user : User) {

        val internalRepositories = RealmList<RepositoryInternal>()
        //val repoCache = RepositoryCache(realmConfig)

        for (repo in user.repositoryList) {
            //repoCache.add(repo)
            internalRepositories.add(
                RepositoryInternal(repo.id, repo.name, repo.visibility, repo.description)
            )
        }

        //why not user.id because id is the private key and user value cannot be trusted to have the correct id
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
}*/

class UserCacheUpdater(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val moshi = Moshi.Builder().build()
        val adapter : JsonAdapter<User> = moshi.adapter(User::class.java)

        val json = inputData.getString("userJson")
        if (json != null) {
            val newUserInfo = adapter.fromJson(json)

            if (newUserInfo != null) {
                println("input id is ${newUserInfo.id}")
                println("input name is ${newUserInfo.name}")

                val userCache = UserCache(DataBase().config)
                userCache.add(newUserInfo)

                val savedUser = userCache.get(newUserInfo.id)
                if (savedUser != null) {
                    println("saved user name ${savedUser.name}")
                }else {
                    println("saved user does not exists")
                }
            }
        }

        return Result.success()
    }

}