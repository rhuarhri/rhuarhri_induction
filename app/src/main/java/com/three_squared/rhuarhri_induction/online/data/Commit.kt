package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class CommitInfo(
    @field:Json(name = "sha") var id : String?,
    @field:Json(name = "commit") var commit : Commit?,
    @field:Json(name = "author") var author : User?,
    @field:Json(name = "committer") var committer : User?
)

data class Commit(
    @field:Json(name = "message") var message : String?,
)




