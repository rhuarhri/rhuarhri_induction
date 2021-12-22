package com.three_squared.rhuarhri_induction.view_commit_screen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewCommitScreenViewModel @Inject constructor(private val repo : ViewCommitScreenRepository) : ViewModel() {

    val repository : MutableLiveData<Repository> by lazy {
        MutableLiveData<Repository>(
            Repository("", "", "", "")
        )
    }

    val refreshingList : MutableLiveData<Boolean> by lazy {
        /*When the user pulls to refresh the commit list a
        little process indicator is displayed. This variable
        is used to hide and display this process indicator.
         */
        MutableLiveData<Boolean>(true)
    }

    private var repositoryOwnerName = ""
    fun getRepositoryOwnerName() : String {
        return repositoryOwnerName
    }

    fun setup(bundle: Bundle?, updateCommitList : (commitList : List<Commit>) -> Unit) {
        val currentRepository = repository.value ?: Repository("", "", "", "")
        if (bundle != null && currentRepository.id.isBlank()) {
            repositoryOwnerName = bundle.getString("ownerName") ?: ""
            val id = bundle.getString("id") ?: ""
            val name = bundle.getString("name") ?: ""
            val visibility = bundle.getString("visibility") ?: ""
            val description = bundle.getString("description") ?: ""

            repository.value = Repository(id, name, visibility, description)

            getCommits(repositoryOwnerName, name, updateCommitList)
        }
    }

    private fun getCommits(userName : String, repositoryName : String, updateCommitList : (commitList : List<Commit>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val foundCommitList = repo.getCommits(userName, repositoryName)
            withContext(Dispatchers.Main) {
                updateCommitList.invoke(foundCommitList)
                refreshingList.value = false
            }
        }
    }

    fun refreshCommits(userName: String, repositoryName : String, updateCommitList: (commitList: List<Commit>) -> Unit) {
        refreshingList.value = true
        getCommits(userName, repositoryName, updateCommitList)
    }
}