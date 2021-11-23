package com.three_squared.rhuarhri_induction.search_screen

import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.online.data.UserOnline
import javax.inject.Inject

class SearchScreenRepository @Inject constructor(
    private val onlineQueryHandler: QueryHandler,
    private val connectionChecker: ConnectionChecker
) {

    suspend fun getUserInfo(userName: String): User {
        val foundUser: UserOnline? = onlineQueryHandler.getUser(userName)

        return if (foundUser != null) {
            val id = foundUser.id ?: ""
            val name = foundUser.name ?: ""
            val avatar = foundUser.avatar ?: ""
            val repoList = foundUser.repoListURL ?: ""
            User(id, repoList, name, avatar)
        } else {
            User("", "", "", "")
        }
    }

    /*suspend fun getUserFromCache(userId : String) : User {
        val foundUsers = cache.getUser(userId)

        return if (foundUsers.isNotEmpty()) {
            foundUsers.first()
        } else {
            User("","","","")
        }
    }

    suspend fun checkConnection() {
        println("Checking connection")
        val connected = connectionChecker.check()
        if (connected == true) {
            println("Connected")
        } else {
            println("Not connected")
        }
    }

    suspend fun addUserToCache(user : User) {

    }*/
}