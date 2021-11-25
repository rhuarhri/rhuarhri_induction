package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.data.RepositoryInternal
import com.three_squared.rhuarhri_induction.storage.data.UserInternal
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class UserCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<User>(realmConfig) {

    suspend fun add(user: User) {

        val internalRepositories = RealmList<RepositoryInternal>()

        for (repository in user.repositoryList) {
            internalRepositories.add(
                RepositoryInternal(repository.id, repository.name, repository.visibility, repository.description)
            )
        }

        val userInternal = UserInternal(id = user.id, name = user.name,
            avatarUrl = user.avatar, repositoryUrl = user.repoListURL, repositories = internalRepositories)

        val foundUser = get(user.id)

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            if (foundUser == null) {
                transaction.insert(userInternal)
            }
        }

        if (foundUser != null) {
            updateInternalUser(user.id, user)
        }
    }

    private suspend fun updateInternalUser(id : String, user : User) {

        val internalRepositories = RealmList<RepositoryInternal>()
        //val repoCache = RepositoryCache(realmConfig)

        for (repo in user.repositoryList) {
            //repoCache.add(repo)
            internalRepositories.add(
                RepositoryInternal(repo.id, repo.name, repo.visibility, repo.description)
            )
        }

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->

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
        }
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

    suspend fun get(userId : String): User? {
        var foundUser : User? = null

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->

            val foundUserInternal =  transaction.where(UserInternal::class.java)
                .equalTo("id", userId)
                .findFirst()
            if (foundUserInternal != null) {
                foundUser = mapToUser(foundUserInternal)
            }

        }

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

    suspend fun clear() {
        val realm = getInstance()
        realm.executeTransactionAwait { transaction ->
            transaction.where(UserInternal::class.java).findAll().deleteAllFromRealm()
        }
    }
}