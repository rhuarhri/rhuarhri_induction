package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val searchScreenRepository: SearchScreenRepository): ViewModel() {

    /*val userInfo : MutableLiveData<User> by lazy {
        MutableLiveData<User>(User("", "", "", "", listOf()))
    }

    val repositoryList : MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>(listOf())
    }*/

    val userInfo = searchScreenRepository.userInfo
    val repositoryList = searchScreenRepository.repositoryList

    fun searchForUser(userName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchScreenRepository.getUserInfo(userName)

            /*withContext(Dispatchers.Main) {
                if (foundUser != null) {
                    userInfo.value = foundUser
                    repositoryList.value = foundUser.repositoryList
                }
            }*/
        }
    }

}