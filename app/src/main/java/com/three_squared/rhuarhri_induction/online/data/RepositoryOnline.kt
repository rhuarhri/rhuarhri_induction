package com.three_squared.rhuarhri_induction.online.data

import com.squareup.moshi.Json

data class RepositoryOnline(
    @field:Json(name = "id") val id : Int?,
    @field:Json(name = "name") val name : String?,
    @field:Json(name = "visibility") val visibility : String?,
    @field:Json(name = "owner") val owner : UserOnline?,
    @field:Json(name = "description") val description : String?,
    @field:Json(name = "contributors_url") val contributors : String?,
    @field:Json(name = "created_at") val created : String?,
    @field:Json(name = "updated_at") val updated : String?, )

/*
branches url  example https://api.github.com/repos/rhuarhri/carbonCleanUp/branches
 */
