package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User

class CacheUpdater(private val context: Context) {

    private val workManger = WorkManager.getInstance(context)
    private val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()

    val TAG = "cacheUpdater"

    private fun updateHistory() : WorkContinuation {
        val workRequest = OneTimeWorkRequestBuilder<CacheHistoryWorker>().setConstraints(constraints).build()
        return workManger.beginUniqueWork(TAG, ExistingWorkPolicy.APPEND, workRequest)
    }

    fun updateUser(user : User) {

        val moshi = Moshi.Builder().build()
        val userAdapter : JsonAdapter<User> = moshi.adapter(User::class.java)

        val data = Data.Builder().putString("userJson", userAdapter.toJson(user)).build()

        val workRequest = OneTimeWorkRequestBuilder<UserCacheUpdater>().setConstraints(constraints).setInputData(data).build()

        //always check if the cache is ok to update
        val historyWorker = updateHistory()

        historyWorker.then(workRequest).enqueue()
    }

    fun updateCommits(commits : List<Commit>) {

        val commitJsonList = mutableListOf<String>()

        val moshi = Moshi.Builder().build()
        val commitAdapter : JsonAdapter<Commit> = moshi.adapter(Commit::class.java)
        for (commit in commits) {
            val commitJson = commitAdapter.toJson(commit)
            commitJsonList.add(commitJson)
        }

        val data = Data.Builder().putStringArray("commitJson", commitJsonList.toTypedArray()).build()

        val workRequest = OneTimeWorkRequestBuilder<CommitCacheUpdater>().setConstraints(constraints).setInputData(data).build()

        //always check if the cache is ok to update
        val historyWorker = updateHistory()

        historyWorker.then(workRequest).enqueue()
    }

}