package com.three_squared.rhuarhri_induction.storage.data


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RepositoryInternal (
    @PrimaryKey
    @Required
    var id : String = "",
    @Required
    var name : String = "",
    @Required
    var visibility : String = "",
    @Required
    var description : String = "",
) : RealmObject()