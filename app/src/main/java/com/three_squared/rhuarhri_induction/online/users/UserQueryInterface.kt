package com.three_squared.rhuarhri_induction.online.users

import com.three_squared.rhuarhri_induction.online.privateTokens.githubToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserQueryInterface {

    @Headers("Authorization: token $githubToken")
    @GET("users/{name}")
    fun getUser(@Path(value = "name") name : String): Call<OnlineUser>
}