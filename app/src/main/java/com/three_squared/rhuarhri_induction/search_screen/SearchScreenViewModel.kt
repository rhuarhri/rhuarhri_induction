package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val searchScreenRepository: SearchScreenRepository): ViewModel() {

    //val refreshed = "refreshed 00:00:00"

    //val searchListResult : List<String> = listOf("test 1", "test 2", "test 3", "test 4")

    //val foundUser = searchScreenRepository.userInfo
    //val foundRepositoryList = searchScreenRepository.repositoryList

    val userInfo : MutableLiveData<User> by lazy {
        MutableLiveData<User>(User("", "", "", ""))
    }

    val repositoryList : MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>(listOf<Repository>())
    }

    fun searchForUser(userName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val foundUser = searchScreenRepository.getUserInfo(userName)

            withContext(Dispatchers.Main) {
                userInfo.value = foundUser
            }
        }
    }

}