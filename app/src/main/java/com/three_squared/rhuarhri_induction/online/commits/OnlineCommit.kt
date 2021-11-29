package com.three_squared.rhuarhri_induction.online.commits

import com.squareup.moshi.Json

data class OnlineCommit(
    @field:Json(name = "sha") val id : String?,
    @field:Json(name = "commit") val commit : Commit?,
    @field:Json(name = "parents") val previous : List<PreviousCommit>?,
    @field:Json(name = "author") val author : UserInfo?,
    @field:Json(name = "committer") val committer : UserInfo?
)

data class Commit(
    @field:Json(name = "message") val message : String?,
    @field:Json(name = "committer") val committer : CommitUserInfo?,
    @field:Json(name = "author") val author : CommitUserInfo?
)

data class CommitUserInfo(
    @field:Json(name = "name") val name : String?,
    @field:Json(name = "email") val email : String?
)

data class UserInfo(
    @field:Json(name = "login") val name : String?,
    @field:Json(name = "id") val id : String?,
    @field:Json(name = "avatar_url") val avatarUrl : String?
)

data class PreviousCommit(
    @field:Json(name = "sha") val id : String,
)