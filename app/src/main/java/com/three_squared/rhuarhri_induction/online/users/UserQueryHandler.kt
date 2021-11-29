package com.three_squared.rhuarhri_induction.online.users

import com.three_squared.rhuarhri_induction.data.User
import retrofit2.Retrofit
import retrofit2.awaitResponse

class UserQueryHandler constructor(private val retroFit : Retrofit) {

    suspend fun getUser(name : String) : User {
        val retrofitInterface = retroFit.create(UserQueryInterface::class.java)

        return try {
            val response = retrofitInterface.getUser(name).awaitResponse()

            if (response.isSuccessful) {
                val userOnline = response.body()
                toUser(userOnline)
            } else {
                User("","","","", listOf())
            }
        } catch (e : Exception) {
            println("error was $e")
            User("","","","", listOf())
        }
    }

    private fun toUser(onlineUser : OnlineUser?) : User {
        return if (onlineUser != null) {
            val id = onlineUser.id ?: ""
            val repositoryURL = onlineUser.repoListURL ?: ""
            val avatarURL = onlineUser.avatar ?: ""
            val name = onlineUser.name ?: ""

            User(id, repositoryURL, name, avatarURL, listOf())
        } else {
            User("","","","", listOf())
        }
    }
}