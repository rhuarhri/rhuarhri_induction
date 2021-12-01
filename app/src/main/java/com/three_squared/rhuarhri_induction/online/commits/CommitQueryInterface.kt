package com.three_squared.rhuarhri_induction.online.commits

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CommitQueryInterface {

    //githubToken is not available in this public repository for security reasons
    //commented out ensures that the app can be built @Headers("Authorization: token $githubToken")
    @GET("repos/{name}/{repository}/commits")
    fun getCommits(@Path(value = "name") name : String,
                   @Path(value = "repository") repositoryName : String)
            : Call<List<OnlineCommit>>

    //commented out ensures that the app can be built @Headers("Authorization: token $githubToken")
    @GET("repos/{name}/{repository}/commits/{id}")
    fun getCommitById(@Path(value = "name") userName: String,
                      @Path(value = "repository") repository: String,
                      @Path(value = "id") id : String) : Call<OnlineCommit>
}