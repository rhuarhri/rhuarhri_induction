package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class QueryHandler @Inject constructor(private val retroFit : Retrofit) {

    suspend fun getUser(name : String) : User? {
        val retrofitInterface = retroFit.create(RetroFitInterface::class.java)

        try {
            val response = retrofitInterface.getUser(name).awaitResponse()

            if (response.isSuccessful) {
                val userOnline = response.body()
                if (userOnline != null) {
                    return if (userOnline.id != null &&
                        userOnline.name != null &&
                        userOnline.avatar != null &&
                        userOnline.repoListURL != null) {

                        val repositories = getRepositories(userOnline.name)

                        User(
                            userOnline.id, userOnline.repoListURL,
                            userOnline.name, userOnline.avatar, repositories
                        )
                    } else {
                        null
                    }
                } else {
                    return null
                }
            } else {
                return null
            }
        } catch (e : Exception) {
            println("error was ${e}")
            return null
        }
    }

    suspend fun getRepositories(userName : String) : List<Repository> {
        val retrofitInterface = retroFit.create(RetroFitInterface::class.java)

        try {
            val response = retrofitInterface.getRepositories(userName).awaitResponse()

            if (response.isSuccessful) {
                val foundRepositories = response.body()

                if (foundRepositories != null) {
                    val userRepositories = mutableListOf<Repository>()
                    for (foundRepository in foundRepositories) {

                        if (foundRepository.id != null &&
                            foundRepository.name != null &&
                            foundRepository.visibility != null &&
                            foundRepository.description != null
                        ) {

                            userRepositories.add(
                                Repository(
                                    foundRepository.id.toString(),
                                    foundRepository.name,
                                    foundRepository.visibility,
                                    foundRepository.description
                                )
                            )
                        }
                    }

                    return userRepositories
                } else {
                    return listOf()
                }
            } else {
                return listOf()
            }
        } catch (e : Exception) {
            println("error was ${e}")
            return listOf()
        }
    }
}
