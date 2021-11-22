package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.data.UserInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RealmHandler @Inject constructor(private val realmConfig : RealmConfiguration) {


    suspend fun addUser(user : User) {

        val realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAwait { transaction ->
            val userInternal = UserInternal(id = user.id, name = user.name,
                avatarUrl = user.avatar, repositoryUrl = user.repoListURL)
            transaction.insert(userInternal)
        }
    }

    suspend fun getUser(userId : String) : List<User> {

        val foundUsers = mutableListOf<User>()

        val realm = Realm.getInstance(realmConfig)
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
}