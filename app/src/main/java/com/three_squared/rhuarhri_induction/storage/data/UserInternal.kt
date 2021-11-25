package com.three_squared.rhuarhri_induction.storage.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class UserInternal (
    @PrimaryKey
    //var primaryKey : String = ObjectId().toHexString(), //TODO remove primary key
    @Required
    var id : String = "",
    @Required
    var name : String = "",
    @Required
    var avatarUrl : String = "",
    @Required
    var repositoryUrl : String = "",
    var repositories : RealmList<RepositoryInternal> = RealmList()
) : RealmObject()