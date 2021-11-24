package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.Commit
import io.realm.RealmConfiguration
import javax.inject.Inject

class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<Commit>(realmConfig) {
    suspend fun add(commit : Commit) {
        TODO("Not yet implemented")
    }

    suspend fun get(): List<Commit> {
        TODO("Not yet implemented")
    }

    suspend fun clear() {
        TODO("Not yet implemented")
    }
}