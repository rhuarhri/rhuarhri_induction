package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import io.realm.Realm
import io.realm.RealmConfiguration
import java.time.LocalDate

class CacheHistoryManager(private val config: RealmConfiguration) {

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
        //ensure history is clear
        realm.delete(CacheHistory::class.java)

        val currentTime = LocalDate.now().toEpochDay()
        val newHistory = CacheHistory(time = currentTime)
        realm.insert(newHistory)

        realm.commitTransaction()
    }
}