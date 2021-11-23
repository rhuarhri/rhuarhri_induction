package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.data.UserInternal
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class UserCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<User>(realmConfig) {

    override suspend fun add(user: User) {
        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            val userInternal = UserInternal(id = user.id, name = user.name,
                avatarUrl = user.avatar, repositoryUrl = user.repoListURL)
            transaction.insert(userInternal)
        }
    }

    suspend fun get(userId : String): List<User> {
        val foundUsers = mutableListOf<User>()

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->

            foundUsers.addAll(transaction.where(UserInternal::class.java)
                .equalTo("id", userId)
                .findAll()
                .map {
                    User(it.id, it.repositoryUrl, it.name, it.avatarUrl)
                }
            )
        }

        return foundUsers
    }

    suspend fun getByName(userName : String) : List<User> {
        val foundUsers = mutableListOf<User>()

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            foundUsers.addAll(transaction.where(UserInternal::class.java).equalTo("name", userName).findAll().map {
                User(it.id, it.repositoryUrl, it.name, it.avatarUrl)
            })
        }

        return foundUsers
    }

    override suspend fun clear() {
        val realm = getInstance()
        realm.executeTransactionAwait { transaction ->
            transaction.where(UserInternal::class.java).findAll().deleteAllFromRealm()
        }
    }
}