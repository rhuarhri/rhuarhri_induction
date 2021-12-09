package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User

const val TAG = "cacheUpdater"
class CacheUpdater(context: Context) {

    /*
    App Presentation WorkManager
    This class uses a library called work manager. WorkManager is designed to allow
    an android app to run long running tasks and offers advantages such as continuing
    to run even when the app has closed and restarting work when a smartphone is
    turned off. However the main disadvantage is that you may not know
    when the task (that work manager is working on will start) will start. For
    example the app uses work manager to updated the apps database. If the user closes
    and turns off there smartphone as the database is being updated then this runs the
    risk of updating the database with out of date information when the smartphone is
    turned back on.

    The main reason work manager is used in this app is that
    ensures that the work gets completed, even if the app is closed.
     */

    private val workManger = WorkManager.getInstance(context)
    private val constraints = Constraints.Builder().setRequiresBatteryNotLow(true).build()

    private fun updateHistory() : WorkContinuation {
        val workRequest = OneTimeWorkRequestBuilder<CacheHistoryWorker>().setConstraints(constraints).build()
        return workManger.beginUniqueWork(TAG, ExistingWorkPolicy.APPEND, workRequest)
    }

    /* why no repository updater?
    this is because the user data class has a list of repositories as a variable
    so when the user is updated so will be that user's list of repositories
     */

    fun updateUser(user : User) {

        val moshi = Moshi.Builder().build()
        val userAdapter : JsonAdapter<User> = moshi.adapter(User::class.java)

        val data = Data.Builder().putString(UserCacheUpdaterKey, userAdapter.toJson(user)).build()

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

        val data = Data.Builder().putStringArray(CommitCacheUpdaterKey, commitJsonList.toTypedArray()).build()

        val workRequest = OneTimeWorkRequestBuilder<CommitCacheUpdater>().setConstraints(constraints).setInputData(data).build()

        //always check if the cache is ok to update
        val historyWorker = updateHistory()

        historyWorker.then(workRequest).enqueue()
    }

}