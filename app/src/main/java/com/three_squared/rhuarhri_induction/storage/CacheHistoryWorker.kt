package com.three_squared.rhuarhri_induction.storage

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class CacheHistoryWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        val dataBase = DataBase()
        val historyManager = CacheHistoryManager(dataBase.config)

        return try {
            val hasCacheExpired = historyManager.hasExpired()

            if (hasCacheExpired) {
                dataBase.deleteDataBase()
                historyManager.reset()
            }

            Result.success()
        } catch(e : Exception) {
            println("Cache history worker exception is $e")
            Result.failure()
        }
    }

}


/*class HasCacheExpiredWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {



        return Result.success()
    }
}

class UpdateHistoryWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        return Result.success()
    }

}*/