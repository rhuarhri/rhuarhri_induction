package com.three_squared.rhuarhri_induction.online.repositories

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryQueryInterface {

    //githubToken is not available in this public repository for security reasons
    //commented out ensures that the app can be built @Headers("Authorization: token $githubToken")
    @GET("users/{name}/repos")
    fun getRepositories(@Path(value = "name") name : String): Call<List<OnlineRepository>>
}