package com.three_squared.rhuarhri_induction.view_commit_screen

import android.content.Context
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import com.three_squared.rhuarhri_induction.storage.CacheUpdater
import com.three_squared.rhuarhri_induction.storage.CommitCache
import javax.inject.Inject

class ViewCommitScreenRepository @Inject constructor(
    private val context : Context,
    private val queryHandler: QueryHandler,
    private val commitCache : CommitCache,
    private val connectionChecker: ConnectionChecker){

    suspend fun getCommits(userName : String, repositoryName : String) : List<Commit> {

        when(connectionChecker.check()) {
            ConnectionType.STRONG -> {
                //online only
                val onlineCommits = queryHandler.getCommits(userName, repositoryName)


                if (onlineCommits.isNotEmpty()) {
                    val updater = CacheUpdater(context)
                    updater.updateCommits(onlineCommits)
                }

                return onlineCommits
            }
            ConnectionType.WEAK -> {
                //cache first
                val cachedCommits = commitCache.getByRepositoryName(repositoryName)

                return if (cachedCommits.isNotEmpty()) {
                    cachedCommits
                } else {
                    val onlineCommits = queryHandler.getCommits(userName, repositoryName)

                    if (onlineCommits.isNotEmpty()) {
                        val updater = CacheUpdater(context)
                        updater.updateCommits(onlineCommits)
                    }

                    onlineCommits
                }

            }
            ConnectionType.NONE -> {
                //cache only
                return commitCache.getByRepositoryName(repositoryName)
            }
        }

    }

}