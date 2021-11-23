package com.three_squared.rhuarhri_induction.online

import com.three_squared.rhuarhri_induction.online.data.UserOnline
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class QueryHandler @Inject constructor(private val retroFit : Retrofit) {

    suspend fun getUser(name : String) : UserOnline? {
        val retrofitInterface = retroFit.create(RetroFitInterface::class.java)

        println("Input name is $name")

        return try {
            val response = retrofitInterface.getUser(name).awaitResponse()

            if (response.isSuccessful) {
                println("result name was ${response.body()?.name.toString()}")
                response.body()
            } else {
                println("query not successful")
                null
            }
        } catch (e : Exception) {
            println("error was ${e}")
            null
        }
    }
}
