package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class Repository(
    @field:Json(name = "id") var id : Int?,
    @field:Json(name = "name") var name : String?,
    @field:Json(name = "visibility") var visibility : Boolean?,
    @field:Json(name = "owner") var owner : User?,
    @field:Json(name = "description") var description : String?,
    @field:Json(name = "contributors_url") var contributors : String?,
    @field:Json(name = "created_at") var created : String?,
    @field:Json(name = "updated_at") var updated : String?, )

/*
branches url  example https://api.github.com/repos/rhuarhri/carbonCleanUp/branches
 */
