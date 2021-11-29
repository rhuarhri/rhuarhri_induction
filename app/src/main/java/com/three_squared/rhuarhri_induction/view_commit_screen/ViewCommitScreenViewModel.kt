package com.three_squared.rhuarhri_induction.view_commit_screen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewCommitScreenViewModel @Inject constructor(private val repo : ViewCommitScreenRepository) : ViewModel() {

    val repository : MutableLiveData<Repository> by lazy {
        MutableLiveData<Repository>(
            Repository("", "", "", "")
        )
    }

    val commitList = repo.commitList

    private var repositoryOwnerName = ""
    fun getRepositoryOwnerName() : String {
        return repositoryOwnerName
    }

    fun setup(bundle: Bundle?) {
        if (bundle != null) {
            repositoryOwnerName = bundle.getString("ownerName") ?: ""
            val id = bundle.getString("id") ?: ""
            val name = bundle.getString("name") ?: ""
            val visibility = bundle.getString("visibility") ?: ""
            val description = bundle.getString("description") ?: ""

            repository.value = Repository(id, name, visibility, description)
        }
    }

    fun getCommits(userName : String, repositoryName : String) {
        if (repo.commitList.value.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.getCommits(userName, repositoryName)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        repo.commitList.value = listOf()
    }
}