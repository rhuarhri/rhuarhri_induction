package com.three_squared.rhuarhri_induction.online.commits

import retrofit2.Retrofit
import retrofit2.awaitResponse
import java.lang.Exception
import com.three_squared.rhuarhri_induction.data.Commit


class CommitQueryHandler constructor(private val retroFit: Retrofit ) {

    suspend fun getMainBranchCommits(userName: String, repository: String) : List<Commit> {
        /*
        This function gets the commits from the master branch only.
        to get commits that are branch specific then use the getCommits function
         */

        val commitList = mutableListOf<Commit>()

        val retrofitInterface = retroFit.create(CommitQueryInterface::class.java)

        val response = retrofitInterface.getCommits(userName, repository).awaitResponse()

        if (response.isSuccessful) {
            val onlineCommits = response.body()

            if (onlineCommits != null) {
                for (onlineCommit in onlineCommits) {
                    commitList.add(toCommit(onlineCommit, repository))
                }
            }
        }

        return commitList
    }

    suspend fun getCommits(userName: String, repository: String, firstCommitId : String) : List<Commit> {

        val commitList = mutableListOf<Commit>()

        var commitId = firstCommitId

        while (commitId != "") {
            val onlineCommit = getCommit(userName, repository, commitId)

            commitList.add(toCommit(onlineCommit, repository))

            commitId = if (onlineCommit != null) {
                val previousCommitsList = onlineCommit.previous ?: listOf()

                /*
                Each commit has a reference to the commit that came before it
                and if the commit dose not contain a reference then it is the first commit
                in the branch
                 */
                if (previousCommitsList.isNotEmpty()) {
                    val previousCommit = previousCommitsList.first()
                    previousCommit.id
                } else {
                    ""
                }
            } else {
                ""
            }
        }

        return commitList
    }

    suspend fun getCommitById(userName : String, repository : String, id : String) : Commit {
        val onlineCommit = getCommit(userName, repository, id)
        return toCommit(onlineCommit, repository)
    }

    private suspend fun getCommit(userName : String, repository : String, id : String) : OnlineCommit? {
        val retrofitInterface = retroFit.create(CommitQueryInterface::class.java)

        return try {
            val response = retrofitInterface.getCommitById(userName, repository, id).awaitResponse()

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        }catch (e : Exception) {
            println("error in getCommit in CommitQueryHandler was $e")
            null
        }
    }

    private fun toCommit(onlineCommit : OnlineCommit?, repository: String) : Commit {

        if (onlineCommit != null) {
            val commitId = onlineCommit.id ?: ""

            val commitInfo = onlineCommit.commit

            var commitMessage = ""
            var committerName = ""
            if (commitInfo != null) {
                commitMessage = commitInfo.message ?: ""

                val committerInfo = commitInfo.committer
                if (committerInfo != null) {
                    committerName = committerInfo.name ?: ""
                }
            }

            var committerId = ""
            var committerAvatarURL = ""
            val committerInfo = onlineCommit.committer

            if (committerInfo != null) {
                committerName = committerInfo.name ?: ""
                committerId = committerInfo.id ?: ""
                committerAvatarURL = committerInfo.avatarUrl ?: ""
            }

            return Commit(commitId, committerName, committerId, committerAvatarURL, commitMessage, repository)
        } else {
            return Commit("","","", "", "", repository)
        }
    }
}