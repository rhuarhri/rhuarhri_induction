package com.three_squared.rhuarhri_induction.storage.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class CacheHistory (
    @PrimaryKey
    var primaryKey : String = ObjectId().toHexString(),//TODO remove primary key
    var time : Long = 0,
) : RealmObject()