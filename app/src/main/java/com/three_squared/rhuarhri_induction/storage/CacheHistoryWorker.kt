package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.three_squared.rhuarhri_induction.dependency_injection.Dependencies
import java.lang.Exception

class CacheHistoryWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        val historyManager = CacheHistoryManager(Dependencies().providesRealmConfig())

        return try {
            val hasCacheExpired = historyManager.hasExpired()

            if (hasCacheExpired) {
                historyManager.reset()
            }

            Result.success()
        } catch(e : Exception) {
            println("Cache history worker exception is $e")
            Result.failure()
        }
    }

}