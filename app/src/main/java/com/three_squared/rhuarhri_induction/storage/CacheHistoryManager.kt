package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import io.realm.Realm
import io.realm.RealmConfiguration
import java.time.LocalDate

class CacheHistoryManager(private val config: RealmConfiguration) {

    /*
    This checks the age of the cache, as if it is too old it will be filled
    with rarely used data. If it is too old the reset() will clear the database
    before resetting the age of the cache
     */

    private val cacheLifeInDays : Long = 2

    fun hasExpired() : Boolean {
        val realm = Realm.getInstance(config)

        if (realm.isEmpty) {
            return false
        } else {
            val history = mutableListOf<Long>()

            realm.beginTransaction()
            history.addAll(realm.where(CacheHistory::class.java).sort("time").findAll().map {
                it.time
            })
            realm.commitTransaction()

            if (history.isEmpty()) {
                return false
            }

            val cacheMaxLife = LocalDate.now().minusDays(cacheLifeInDays)

            val cacheCreated = LocalDate.ofEpochDay(history.first())

            return !cacheMaxLife.isBefore(cacheCreated)

        }
    }

    fun reset() {
        val realm = Realm.getInstance(config)

        realm.beginTransaction()
        //ensure database is clear
        realm.deleteAll()

        val currentTime = LocalDate.now().toEpochDay()
        val newHistory = CacheHistory(time = currentTime)
        realm.insert(newHistory)

        realm.commitTransaction()
    }
}