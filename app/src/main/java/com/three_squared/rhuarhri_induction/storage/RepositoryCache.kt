package com.three_squared.rhuarhri_induction.storage

import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.storage.data.RepositoryInternal
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import javax.inject.Inject

class RepositoryCache @Inject constructor(realmConfig : RealmConfiguration) : CacheParent<Repository>(realmConfig) {
    suspend fun add(repository : Repository) {


        val repositoryInternal = RepositoryInternal(id = repository.id, name = repository.name,
            visibility = repository.visibility, description = repository.description)

        val foundRepository = getById(repository.id)

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            if (foundRepository == null) {
                transaction.insert(repositoryInternal)
            }
        }

        if (foundRepository != null) {
            updateRepository(repository.id, repository)
        }
    }

    private suspend fun updateRepository(id : String, repository: Repository) {
        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            val foundRepositoryInternal = transaction.where(RepositoryInternal::class.java)
                .equalTo("id", id)
                .findFirst()

            if (foundRepositoryInternal != null) {
                foundRepositoryInternal.name = repository.name
                foundRepositoryInternal.description = repository.description
                foundRepositoryInternal.visibility = repository.visibility
            }
        }
    }

    suspend fun getById(id : String) : Repository? {
        var foundRepository : Repository? = null

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            val foundRepositoryInternal = transaction.where(RepositoryInternal::class.java)
                .equalTo("id", id)
                .findFirst()
            if (foundRepositoryInternal != null) {
                foundRepository = mapToRepository(foundRepositoryInternal)
            }
        }

        return foundRepository
    }

    suspend fun get(): List<Repository> {
        val repoList = mutableListOf<Repository>()

        val realm = super.getInstance()
        realm.executeTransactionAwait { transaction ->
            repoList.addAll(transaction.where(RepositoryInternal::class.java).findAll().map {
                mapToRepository(it)
            })
        }

        return repoList
    }

    private fun mapToRepository(repositoryInternal: RepositoryInternal) : Repository {
        return Repository(repositoryInternal.id, repositoryInternal.name,
            repositoryInternal.visibility, repositoryInternal.description)
    }

    suspend fun clear() {

    }
}