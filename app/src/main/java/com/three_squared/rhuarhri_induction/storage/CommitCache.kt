package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.*
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import com.three_squared.rhuarhri_induction.storage.data.CommitInternal
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<Commit>(realmConfig) {
    suspend fun add(commit : Commit) {

        val foundCommit = getById(commit.commitId)

        if (foundCommit == null) {
            val commitInternal = CommitInternal(commit.commitId, commit.committerId,
                commit.committerName, commit.committerAvatar, commit.message, commit.repositoryName)
            val realm = super.getInstance()
            realm.executeTransactionAwait { transaction ->
                transaction.insert(commitInternal)
            }
        } else {
            updateCommit(commit.commitId, commit)
        }
    }

    private suspend fun updateCommit(id : String, commit : Commit) {
        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            val foundCommit = transaction.where(CommitInternal::class.java).equalTo("id", id).findFirst()

            if (foundCommit != null) {
                foundCommit.committerId = commit.committerId
                foundCommit.committerName = commit.committerName
                foundCommit.committerAvatar = commit.committerAvatar
                foundCommit.message = commit.message
                foundCommit.repositoryName = commit.repositoryName
            }
        }
    }

    suspend fun getById(id : String) : Commit? {
        var commit : Commit? = null

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
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
        }

        return commit
    }

    suspend fun getByRepositoryName(name : String) : List<Commit> {
        val commitList = mutableListOf<Commit>()

        val realm = super.getInstance()
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

    suspend fun update(commits : List<Commit>) {
        val expired = super.hasCacheExpired()
        if (expired) {
            super.deleteAll()
        }

        for (commit in commits) {
            add(commit)
        }
    }

    fun updateCacheWorker(context : Context) {
        val workManger = WorkManager.getInstance(context)
        val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()
        val data = Data.Builder().putString("name", "Fred").build()
        val workRequest = OneTimeWorkRequestBuilder<Updater>().setConstraints(constraints).setInputData(data).build()
        workManger.beginWith(workRequest).enqueue()
    }
}

class Updater(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        val name = inputData.getString("name")

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .build()
        val realm = Realm.getInstance(config)

        val history = mutableListOf<Long>()

        realm.beginTransaction()

        history.addAll(realm.where(CacheHistory::class.java).sort("time")
            .findAll()
            .map {
                it.time
            })

        realm.close()

        println("the name is $name")
        println("work manager is here")
        if (history.isEmpty()) {
            println("No history")
        } else {
            println("History found")
        }
        return Result.success()
    }

    suspend fun test() {

    }
}