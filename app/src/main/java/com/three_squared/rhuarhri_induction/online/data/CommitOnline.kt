package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class CommitOnline(
    @field:Json(name = "sha") val id : String?,
    @field:Json(name = "commit") val commit : Commit?,
    //@field:Json(name = "author") val author : UserOnline?,
    //@field:Json(name = "committer") val committer : UserOnline?
)

data class Commit(
    @field:Json(name = "message") val message : String?,
    @field:Json(name = "committer") val committer : UserInfo?,
    @field:Json(name = "author") val author : UserInfo?
)

data class UserInfo(
    @field:Json(name = "name") val name : String?,
    @field:Json(name = "email") val email : String?
)




