package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.dependency_injection.Dependencies
import com.three_squared.rhuarhri_induction.storage.data.CommitInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class CommitCache @Inject constructor(private val realmConfig : RealmConfiguration) {
    fun add(commit : Commit) {

        val foundCommit = getById(commit.commitId)

        if (foundCommit == null) {
            val commitInternal = CommitInternal(commit.commitId, commit.committerId,
                commit.committerName, commit.committerAvatar, commit.message, commit.repositoryName)
            val realm = Realm.getInstance(realmConfig)
            realm.beginTransaction()
            realm.insert(commitInternal)
            realm.commitTransaction()
        } else {
            updateCommit(commit.commitId, commit)
        }
    }

    private fun updateCommit(id : String, commit : Commit) {
        val realm = Realm.getInstance(realmConfig)

        realm.beginTransaction()
        val foundCommit = realm.where(CommitInternal::class.java).equalTo("id", id).findFirst()
        if (foundCommit != null) {
            foundCommit.committerId = commit.committerId
            foundCommit.committerName = commit.committerName
            foundCommit.committerAvatar = commit.committerAvatar
            foundCommit.message = commit.message
            foundCommit.repositoryName = commit.repositoryName
        }
        realm.commitTransaction()
    }

    fun getById(id : String) : Commit? {
        var commit : Commit? = null

        val realm = Realm.getInstance(realmConfig)
        realm.beginTransaction()
        val foundCommit = realm.where(CommitInternal::class.java).equalTo("id", id).findFirst()
        realm.commitTransaction()

        if (foundCommit != null) {
            commit = Commit(
                foundCommit.id,
                foundCommit.committerName,
                foundCommit.committerId,
                foundCommit.committerAvatar,
                foundCommit.message,
                foundCommit.repositoryName
            )
        }

        return commit
    }

    suspend fun getByRepositoryName(name : String) : List<Commit> {
        val commitList = mutableListOf<Commit>()

        val realm = Realm.getInstance(realmConfig)
        realm.executeTransactionAwait { transaction ->
            val foundCommits = transaction.where(CommitInternal::class.java)
                .equalTo("repositoryName", name).findAll()

            for (internalCommit in foundCommits) {
                commitList.add(
                    Commit(internalCommit.id,
                        internalCommit.committerName,
                        internalCommit.committerId,
                        internalCommit.committerAvatar,
                        internalCommit.message,
                        internalCommit.repositoryName)
                )
            }
        }

        return commitList
    }
}

const val CommitCacheUpdaterKey = "commitJson"
class CommitCacheUpdater(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val moshi = Moshi.Builder().build()
        val adapter : JsonAdapter<Commit> = moshi.adapter(Commit::class.java)

        val commitJsonList = inputData.getStringArray(CommitCacheUpdaterKey)

        val commitList = mutableListOf<Commit>()

        if (commitJsonList != null) {
            for (commitJson in commitJsonList) {
                val foundCommit = adapter.fromJson(commitJson)
                if (foundCommit != null) {
                    commitList.add(foundCommit)
                }
            }
        }

        val commitCache = CommitCache(Dependencies().providesRealmConfig())
        for (commit in commitList) {
            commitCache.add(commit)
        }

        return Result.success()
    }

}