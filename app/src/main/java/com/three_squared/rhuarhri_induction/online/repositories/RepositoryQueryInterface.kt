package com.three_squared.rhuarhri_induction.online.repositories

import com.three_squared.rhuarhri_induction.online.privateTokens.githubToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RepositoryQueryInterface {

    //githubToken is not available in this public repository for security reasons
    @Headers("Authorization: token $githubToken")
    @GET("users/{name}/repos")
    fun getRepositories(@Path(value = "name") name : String): Call<List<OnlineRepository>>
}