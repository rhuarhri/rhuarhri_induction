package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.MutableLiveData
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.online.data.UserOnline
import javax.inject.Inject

class SearchScreenRepository @Inject constructor(private val onlineQueryHandler: QueryHandler) {

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
}