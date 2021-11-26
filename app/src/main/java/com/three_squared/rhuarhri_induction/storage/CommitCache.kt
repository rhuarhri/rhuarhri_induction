package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.storage.data.CommitInternal
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<Commit>(realmConfig) {
    suspend fun add(commit : Commit) {

        val foundCommit = getById(commit.commitId)

        if (foundCommit == null) {
            val commitInternal = CommitInternal(commit.commitId, commit.committerId,
                commit.committerName, commit.committerAvatar, commit.message)
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
                    foundCommit.message
                )
            }
        }

        return commit
    }

    suspend fun clear() {
        TODO("Not yet implemented")
    }
}