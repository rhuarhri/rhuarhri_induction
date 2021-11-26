package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.MutableLiveData
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.storage.UserCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchScreenRepository @Inject constructor(
    private val onlineQueryHandler: QueryHandler,
    private val connectionChecker: ConnectionChecker,
    private val userCache : UserCache
) {

    val userInfo : MutableLiveData<User> by lazy {
        MutableLiveData<User>(User("", "", "", "", listOf()))
    }

    val repositoryList : MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>(listOf())
    }

    suspend fun getUserInfo(userName: String) {

        when(connectionChecker.check()) {
            ConnectionType.STRONG -> {
                //online only
                val foundUser = onlineQueryHandler.getUser(userName)

                updateLiveData(foundUser)

                if (foundUser != null) {
                    userCache.update(listOf(foundUser))
                }
            }
            ConnectionType.WEAK -> {
                //cache first
                val usersInCache = userCache.getByName(userName)

                if (usersInCache.isNotEmpty()) {
                    updateLiveData(usersInCache.first())
                } else {
                    val foundUser = onlineQueryHandler.getUser(userName)

                    updateLiveData(foundUser)

                    if (foundUser != null) {
                        userCache.update(listOf(foundUser))
                    }
                }
            }
            ConnectionType.NONE -> {
                // cache only
                val usersInCache = userCache.getByName(userName)
                if (usersInCache.isNotEmpty()) {
                    updateLiveData(usersInCache.first())
                }
            }
        }
    }

    private suspend fun updateLiveData(user : User?) {
        withContext(Dispatchers.Main) {
            if (user != null) {
                userInfo.value = user
                repositoryList.value = user.repositoryList
            }
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