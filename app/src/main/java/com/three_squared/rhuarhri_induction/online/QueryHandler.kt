package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.data.Commit
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
            println("error was $e")
            return null
        }
    }

    private suspend fun getRepositories(userName : String) : List<Repository> {
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
            println("error was $e")
            return listOf()
        }
    }

    suspend fun getCommits(userName: String, repositoryName : String) : List<Commit> {
        val retrofitInterface = retroFit.create(RetroFitInterface::class.java)

        try {
            val response = retrofitInterface.getCommits(userName, repositoryName).awaitResponse()

            return if (response.isSuccessful) {
                val foundOnlineCommits = response.body()

                if (foundOnlineCommits != null) {
                    val foundCommits = mutableListOf<Commit>()

                    for (onlineCommit in foundOnlineCommits) {

                        val committerName = if (onlineCommit.commit != null) {
                            if (onlineCommit.commit.committer != null) {
                                onlineCommit.commit.committer.name ?: ""
                            } else {
                                ""
                            }
                        } else {
                            ""
                        }

                        val committer = getUser(committerName) ?: User("", "", "", "", listOf())

                        val commitId = onlineCommit.id ?: ""

                        //val committerName = committer?.name ?: ""

                        val committerId = committer.id

                        val committerAvatar = committer.avatar

                        val commitMessage = onlineCommit.commit?.message ?: ""

                        println("committer name was $committerName on commit with message of $commitMessage with avatar utl of $committerAvatar")

                        foundCommits.add(
                            Commit(
                                commitId,
                                committerName,
                                committerId,
                                committerAvatar,
                                commitMessage,
                                repositoryName
                            )
                        )
                    }

                    return foundCommits

                } else {
                    listOf()
                }
            } else {
                listOf()
            }
        } catch (e : Exception) {
            println("error was $e")
            return listOf()
        }
    }
}
