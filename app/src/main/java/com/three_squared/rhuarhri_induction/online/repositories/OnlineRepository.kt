package com.three_squared.rhuarhri_induction.online.repositories

import com.squareup.moshi.Json
import com.three_squared.rhuarhri_induction.online.users.OnlineUser

data class OnlineRepository(
    @field:Json(name = "id") val id : Int?,
    @field:Json(name = "name") val name : String?,
    @field:Json(name = "visibility") val visibility : String?,
    @field:Json(name = "owner") val owner : OnlineUser?,
    @field:Json(name = "description") val description : String?,
    @field:Json(name = "contributors_url") val contributors : String?,
    @field:Json(name = "created_at") val created : String?,
    @field:Json(name = "updated_at") val updated : String?,
)