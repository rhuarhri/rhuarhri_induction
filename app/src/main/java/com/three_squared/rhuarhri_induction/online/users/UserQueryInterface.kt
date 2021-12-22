package com.three_squared.rhuarhri_induction.online.users

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserQueryInterface {

    //githubToken is not available in this public repository for security reasons
    //commented out ensures that the app can be built @Headers("Authorization: token ${Base64.getDecoder().decode(BuildConfig.GITHUB_API_KEY).decodeToString()}")
    @GET("users/{name}")
    fun getUser(@Path(value = "name") name : String): Call<OnlineUser>
}