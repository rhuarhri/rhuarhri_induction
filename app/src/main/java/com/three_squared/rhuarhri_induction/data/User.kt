package com.three_squared.rhuarhri_induction.data

data class User(
    val id : String,
    val repoListURL : String,
    val name : String,
    val avatar : String,
    val repositoryList : List<Repository>
)
