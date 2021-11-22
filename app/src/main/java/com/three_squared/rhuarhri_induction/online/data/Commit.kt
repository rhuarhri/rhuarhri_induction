package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class CommitInfo(
    @field:Json(name = "sha") val id : String?,
    @field:Json(name = "commit") val commit : Commit?,
    @field:Json(name = "author") val author : User?,
    @field:Json(name = "committer") val committer : User?
)

data class Commit(
    @field:Json(name = "message") val message : String?,
)




