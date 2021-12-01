package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import com.three_squared.rhuarhri_induction.storage.data.CommitInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class CommitCache @Inject constructor(private val realmConfig : RealmConfiguration) /*: CacheParent<Commit>(realmConfig)*/ {
    fun add(commit : Commit) {

        val foundCommit = getById(commit.commitId)

        if (foundCommit == null) {
            val commitInternal = CommitInternal(commit.commitId, commit.committerId,
                commit.committerName, commit.committerAvatar, commit.message, commit.repositoryName)
            val realm = Realm.getInstance(realmConfig)
            realm.beginTransaction()
            realm.insert(commitInternal)
            realm.commitTransaction()
            /*realm.executeTransactionAwait { transaction ->
                transaction.insert(commitInternal)
            }*/
        } else {
            updateCommit(commit.commitId, commit)
        }
    }

    private fun updateCommit(id : String, commit : Commit) {
        val realm = Realm.getInstance(realmConfig)//super.getInstance()

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

        /*realm.executeTransactionAwait { transaction ->
            val foundCommit = transaction.where(CommitInternal::class.java).equalTo("id", id).findFirst()

            if (foundCommit != null) {
                foundCommit.committerId = commit.committerId
                foundCommit.committerName = commit.committerName
                foundCommit.committerAvatar = commit.committerAvatar
                foundCommit.message = commit.message
                foundCommit.repositoryName = commit.repositoryName
            }
        }*/
    }

    fun getById(id : String) : Commit? {
        var commit : Commit? = null

        val realm = Realm.getInstance(realmConfig)//super.getInstance()
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

        /*realm.executeTransactionAwait { transaction ->
            val foundCommit = transaction.where(CommitInternal::class.java).equalTo("id", id).findFirst()

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
        }*/

        return commit
    }

    suspend fun getByRepositoryName(name : String) : List<Commit> {
        val commitList = mutableListOf<Commit>()

        val realm = Realm.getInstance(realmConfig)//super.getInstance()
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

    /*suspend fun update(commits : List<Commit>) {
        val expired = super.hasCacheExpired()
        if (expired) {
            super.deleteAll()
        }

        for (commit in commits) {
            add(commit)
        }
    }*/
}

class CommitCacheUpdater(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val moshi = Moshi.Builder().build()
        val adapter : JsonAdapter<Commit> = moshi.adapter(Commit::class.java)

        val commitJsonList = inputData.getStringArray("commitJson")

        val commitList = mutableListOf<Commit>()

        if (commitJsonList != null) {
            for (commitJson in commitJsonList) {
                val foundCommit = adapter.fromJson(commitJson)
                if (foundCommit != null) {
                    commitList.add(foundCommit)
                }
            }
        }

        val commitCache = CommitCache(DataBase().config)
        for (commit in commitList) {
            commitCache.add(commit)
        }

        return Result.success()
    }


}