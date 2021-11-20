package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class User(
    @field:Json(name = "id") val id : String?,
    @field:Json(name = "repos_url") val repoListURL : String?,
    @field:Json(name = "login") val name : String?,
    @field:Json(name = "avatar_url") val avatar : String?,
)
