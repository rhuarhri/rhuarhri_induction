package com.three_squared.rhuarhri_induction.search_screen

import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.storage.UserCache
import javax.inject.Inject

class SearchScreenRepository @Inject constructor(
    private val onlineQueryHandler: QueryHandler,
    private val connectionChecker: ConnectionChecker,
    private val userCache : UserCache
) {

    suspend fun getUserInfo(userName: String) : User {

        when(connectionChecker.check()) {
            ConnectionType.STRONG -> {
                //online only
                val foundUser = onlineQueryHandler.getUser(userName)

                if (foundUser.id.isNotBlank()) {
                    userCache.update(listOf(foundUser))
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
                        userCache.update(listOf(foundUser))
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