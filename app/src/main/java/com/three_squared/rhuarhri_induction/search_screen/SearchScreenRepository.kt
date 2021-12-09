package com.three_squared.rhuarhri_induction.search_screen

import android.content.Context
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.storage.CacheUpdater
import com.three_squared.rhuarhri_induction.storage.UserCache
import javax.inject.Inject

class SearchScreenRepository @Inject constructor(
    private val context : Context,
    private val onlineQueryHandler: QueryHandler,
    private val connectionChecker: ConnectionChecker,
    private val userCache : UserCache
) {

    /* TODO App Presentation 6 Search Screen Repository
    All the repositories in this app will look similar to this
    repository. In this app the main role of the repository
    is to know where information is stored allowing the UI
    to just query the repository and not know where the information
    is came from.
    This repository also handles the cache (which in this app is a realm database)
    and decides based on the quality of the apps connection to the internet how much
    of the cache the app should use.

    At the top of this file you should see the key work @Inject
    This comes from a library called HILT. HILT is used to manage
    dependency injection by automatically generating the instances
    of classes that will be passed into another class in this case the
    search screen repository.


    WorkManager vs coroutines
    The getUserInfo() just below has the suspend keyword. This means
    it can only run in a coroutine which is a form of multi threading.
    However within this function it uses a class called CacheUpdater. This
    class extends a library called WorkManger which is a different form
    of multi threading. This is because of a problem with coroutines.
    Coroutines run for as long as the app is open. This is a problem
    when updating the database as if the app is close as the database
    is being updated then the database will not be updated. This is the
    reason that WorkManager is used as WorkManger ensures what ever it
    is working on gets done, even if the app closes.
     */

    suspend fun getUserInfo(userName: String) : User {

        when(connectionChecker.check()) {
            ConnectionType.STRONG -> {
                //online only
                val foundUser = onlineQueryHandler.getUser(userName)

                if (foundUser.id.isNotBlank()) {
                    val updater = CacheUpdater(context)
                    updater.updateUser(foundUser)
                }

                return foundUser
            }
            ConnectionType.WEAK -> {
                //cache first
                val usersInCache = userCache.getByName(userName)

                return if (usersInCache.isNotEmpty()) {
                    usersInCache.first()
                } else {
                    val foundUser = onlineQueryHandler.getUser(userName)

                    if (foundUser.id.isNotBlank()) {
                        val updater = CacheUpdater(context)
                        updater.updateUser(foundUser)
                    }

                    foundUser
                }
            }
            ConnectionType.NONE -> {
                // cache only
                val usersInCache = userCache.getByName(userName)
                return if (usersInCache.isNotEmpty()) {
                    usersInCache.first()
                } else {
                    User("", "", "", "", listOf())
                }
            }
        }
    }
}