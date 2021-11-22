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

    fun databaseTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User("1", "repoUrl", "Jack", "avatarUrl")

            searchScreenRepository.addUserToCache(user)

            val foundUser = searchScreenRepository.getUserFromCache("1")
            println("found user name is ${foundUser.name}")
        }
    }

    fun checkConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            searchScreenRepository.checkConnection()
        }
    }

}