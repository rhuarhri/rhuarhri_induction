package com.three_squared.rhuarhri_induction.view_commit_screen

import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.data.Commit
import javax.inject.Inject

class ViewCommitScreenRepository @Inject constructor(private val queryHandler: QueryHandler){

    suspend fun getCommits(userName : String, repositoryName : String) : List<Commit> {
        return queryHandler.getCommits(userName, repositoryName)
    }
}