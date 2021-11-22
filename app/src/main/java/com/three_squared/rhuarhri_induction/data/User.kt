package com.three_squared.rhuarhri_induction.data

import com.squareup.moshi.Json

data class User(
    val id : String,
    val repoListURL : String,
    val name : String,
    val avatar : String,
)
