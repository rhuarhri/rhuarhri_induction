package com.three_squared.rhuarhri_induction.view_commit_screen

import androidx.lifecycle.MutableLiveData
import com.three_squared.rhuarhri_induction.online.QueryHandler
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import com.three_squared.rhuarhri_induction.storage.CommitCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ViewCommitScreenRepository @Inject constructor(
    private val queryHandler: QueryHandler,
    private val commitCache : CommitCache,
    private val connectionChecker: ConnectionChecker){

    val commitList : MutableLiveData<List<Commit>> by lazy {
        MutableLiveData<List<Commit>>(listOf())
    }

    suspend fun getCommits(userName : String, repositoryName : String) {

        println("user name is $userName")
        println("repository name $repositoryName")

        when(connectionChecker.check()) {
            ConnectionType.STRONG -> {
                //online only
                println("online only")
                val onlineCommits = queryHandler.getCommits(userName, repositoryName)

                commitCache.update(onlineCommits)

                updateLiveData(onlineCommits)

                /*withContext(Dispatchers.IO) {
                    //commitCache.update(onlineCommits)
                }*/
            }
            ConnectionType.WEAK -> {
                //cache first
                println("cache first")
                val cachedCommits = commitCache.getByRepositoryName(repositoryName)

                if (cachedCommits.isNotEmpty()) {
                    updateLiveData(cachedCommits)
                } else {
                    val onlineCommits = queryHandler.getCommits(userName, repositoryName)

                    updateLiveData(onlineCommits)

                    withContext(Dispatchers.IO) {
                        commitCache.update(onlineCommits)
                    }
                }

            }
            ConnectionType.NONE -> {
                //cache only
                println("cache only")
                updateLiveData(commitCache.getByRepositoryName(repositoryName))
            }
        }

    }

    private suspend fun updateLiveData(commits : List<Commit>) {
        withContext(Dispatchers.Main) {
            commitList.value = commits
        }
    }
}