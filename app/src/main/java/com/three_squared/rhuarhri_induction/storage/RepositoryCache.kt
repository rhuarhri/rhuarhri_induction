package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.storage.data.RepositoryInternal
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class RepositoryCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<Repository>(realmConfig) {
    override suspend fun add(repository : Repository) {
        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            val repositoryInternal = RepositoryInternal(id = repository.id, name = repository.name,
                visibility = repository.visibility, description = repository.description)
            transaction.insert(repositoryInternal)
        }
    }

    override suspend fun get(): List<Repository> {
        val repoList = mutableListOf<Repository>()

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            repoList.addAll(transaction.where(RepositoryInternal::class.java).findAll().map {
                Repository(it.id, it.name, it.visibility, it.description)
            })
        }

        return repoList
    }

    override suspend fun clear() {

    }
}