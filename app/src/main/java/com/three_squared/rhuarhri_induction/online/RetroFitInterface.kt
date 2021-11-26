package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.online.data.CommitOnline
import com.three_squared.rhuarhri_induction.online.data.RepositoryOnline
import com.three_squared.rhuarhri_induction.online.data.UserOnline
import com.three_squared.rhuarhri_induction.online.privateTokens.githubToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetroFitInterface {
    @Headers("Authorization: token $githubToken")
    @GET("users/{name}")
    fun getUser(@Path(value = "name") name : String): Call<UserOnline>

    @Headers("Authorization: token $githubToken")
    @GET("users/{name}/repos")
    fun getRepositories(@Path(value = "name") name : String): Call<List<RepositoryOnline>>

    @Headers("Authorization: token $githubToken")
    @GET("repos/{name}/{repository}/commits")
    fun getCommits(@Path(value = "name") name : String,
                   @Path(value = "repository") repositoryName : String)
    : Call<List<CommitOnline>>
}