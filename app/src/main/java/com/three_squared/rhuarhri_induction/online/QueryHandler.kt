package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.online.commits.CommitQueryHandler
import com.three_squared.rhuarhri_induction.online.repositories.RepositoryQueryHandler
import com.three_squared.rhuarhri_induction.online.users.UserQueryHandler
import retrofit2.Retrofit
import javax.inject.Inject

class QueryHandler @Inject constructor(private val retroFit : Retrofit) {

    suspend fun getUser(name : String) : User {
        val userQueryHandler = UserQueryHandler(retroFit)

        val user = userQueryHandler.getUser(name)

        val repositoryQueryHandler = RepositoryQueryHandler(retroFit)

        val repositories = repositoryQueryHandler.getRepositories(name)

        return User(
            user.id,
            user.repoListURL,
            user.name,
            user.avatar,
            repositories
        )
    }

    suspend fun getCommits(userName: String, repositoryName: String) : List<Commit> {
        val commitQueryHandler = CommitQueryHandler(retroFit)
        return commitQueryHandler.getMainBranchCommits(userName, repositoryName)
    }

}