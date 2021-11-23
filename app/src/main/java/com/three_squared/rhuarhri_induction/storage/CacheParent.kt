package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import java.time.LocalDate

const val cacheLife : Long = 2 //in days
open class CacheParent<T>(private val realmConfig : RealmConfiguration) {

    fun getInstance() : Realm {
        return Realm.getInstance(realmConfig)
    }

    open suspend fun add(data : T) {

    }

    open suspend fun get() : List<T> {
        return listOf()
    }

    open suspend fun clear() {

    }

    suspend fun deleteAll() {
        val realm = getInstance()

        realm.executeTransactionAwait { transaction ->
            transaction.deleteAll()
        }
    }

    suspend fun hasCacheExpired() : Boolean {

        val realm = getInstance()

        if (realm.isEmpty) {
            return false
        } else {
            val history = mutableListOf<Long>()

            realm.executeTransactionAwait { transaction ->
                history.addAll(transaction.where(CacheHistory::class.java).sort("time")
                    .findAll()
                    .map {
                    it.time
                }
                )
            }

            if (history.isEmpty()) {
                return false
            }

            val cacheMaxLife = LocalDate.now().minusDays(cacheLife)

            val cacheCreated = LocalDate.ofEpochDay(history.first())

            return !cacheMaxLife.isBefore(cacheCreated)

        }
    }
}