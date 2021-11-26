package com.three_squared.rhuarhri_induction.storage.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CommitInternal(
    @PrimaryKey
    @Required
    var id : String = "",
    @Required
    var committerId : String = "",
    @Required
    var committerName : String = "",
    @Required
    var committerAvatar : String = "",
    @Required
    var message : String = ""
) : RealmObject()