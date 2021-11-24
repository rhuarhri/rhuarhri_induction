package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.online.data.UserOnline
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetroFitInterface {
    @GET("users/{name}")
    fun getUser(@Path(value = "name") name : String): Call<UserOnline>
}