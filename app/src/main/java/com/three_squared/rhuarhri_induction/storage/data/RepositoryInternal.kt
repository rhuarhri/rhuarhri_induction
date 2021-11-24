package com.three_squared.rhuarhri_induction.storage.data

import com.three_squared.rhuarhri_induction.data.User
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class RepositoryInternal (
    @PrimaryKey
    //var privateKey : String = ObjectId().toHexString(),//TODO remove primary key
    @Required
    var id : String = "",
    @Required
    var name : String = "",
    @Required
    var visibility : String = "",
    @Required
    var description : String = "",
) : RealmObject()