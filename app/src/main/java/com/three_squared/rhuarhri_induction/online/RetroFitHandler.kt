package com.three_squared.rhuarhri_induction.online


/*import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.moshi.MoshiConverterFactory


*//*
This class is used to handle the interactions with retrofit
 *//*
class RetroFitHandler {

    *//*
    useful urls
    getting only user information
    https://api.github.com/users/rhuarhri

    getting all user repos
    https://api.github.com/users/rhuarhri/repos

    getting single repo
    https://api.github.com/repos/rhuarhri/carbonCleanUp

    getting all commits
    https://api.github.com/repos/rhuarhri/carbonCleanUp/commits

    getting user image
    https://avatars.githubusercontent.com/u/26746052?v=4
     *//*


    suspend fun getUserInfo() {
        val retrofit : Retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.github.com/").build()

        val retrofitInterface = retrofit.create(RetroFitInterface::class.java)
        val response =  retrofitInterface.getUser("").awaitResponse()

        if (response.isSuccessful) {
            println("Success")
            println("id is ${response.body()!!.id}")
        } else {
            println("failed")
        }
    }

}*/

/*
data class User(
    @field:Json(name = "id") val id : String?,
    @field:Json(name = "repos_url") val repoListURL : String?,
    @field:Json(name = "login") val name : String?,
    @field:Json(name = "avatar_url") val avatar : String?,
    @field:Json(name = "public_repos") val repos : Int?
)*/

/*interface RetroFitInterface {

    @GET("users/rhuarhri")
    fun getUser(): Call<User>

}*/