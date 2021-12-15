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
        MutableLiveData<User>(User("", "", "", "", listOf()))
    }

    val repositoryList : MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>(listOf())
    }

    val errorMessage : MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val loading : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun searchForUser(userName : String) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val foundUser = searchScreenRepository.getUserInfo(userName)

            withContext(Dispatchers.Main) {
                if (foundUser.id.isNotBlank()) {
                    userInfo.value = foundUser
                    repositoryList.value = foundUser.repositoryList

                    if (foundUser.repositoryList.isEmpty()) {
                        errorMessage.value = "No repositories found"
                    } else {
                        errorMessage.value = ""
                    }
                } else {
                    repositoryList.value = listOf()
                    errorMessage.value = "No user found"
                }

                loading.value = false
            }
        }
    }

}