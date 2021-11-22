package com.three_squared.rhuarhri_induction.storage.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class UserInternal (
    @PrimaryKey
    var privateKey : String = ObjectId().toHexString(),
    @Required
    var id : String = "",
    @Required
    var name : String = "",
    @Required
    var avatarUrl : String = "",
    @Required
    var repositoryUrl : String = ""
) : RealmObject()