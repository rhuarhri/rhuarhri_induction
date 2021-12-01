package com.three_squared.rhuarhri_induction.online.repositories

import com.three_squared.rhuarhri_induction.data.Repository
import retrofit2.Retrofit
import retrofit2.awaitResponse

class RepositoryQueryHandler constructor(private val retroFit : Retrofit) {

    suspend fun getRepositories(userName : String) : List<Repository> {
        val retrofitInterface = retroFit.create(RepositoryQueryInterface::class.java)

        try {
            val response = retrofitInterface.getRepositories(userName).awaitResponse()

            if (response.isSuccessful) {
                val foundRepositories = response.body()

                return if (foundRepositories != null) {
                    val userRepositories = mutableListOf<Repository>()
                    for (foundRepository in foundRepositories) {

                        userRepositories.add(toRepository(foundRepository))
                    }

                    userRepositories
                } else {
                    listOf()
                }
            } else {
                return listOf()
            }
        } catch (e : Exception) {
            println("RepositoryQueryHandler error was $e")
            return listOf()
        }
    }

    private fun toRepository(onlineRepository: OnlineRepository) : Repository {
        val id = (onlineRepository.id ?: 0).toString()
        val name = onlineRepository.name ?: ""
        val visibility = onlineRepository.visibility ?: ""
        val description = onlineRepository.description ?: ""

        return Repository(id, name, visibility, description)
    }
}