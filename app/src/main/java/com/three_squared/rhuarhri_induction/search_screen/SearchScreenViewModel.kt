package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val searchScreenRepository: SearchScreenRepository): ViewModel() {

    //val refreshed = "refreshed 00:00:00"

    //val searchListResult : List<String> = listOf("test 1", "test 2", "test 3", "test 4")

    var foundUser = User("", "", "", "")

    fun searchForUser(userName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            foundUser = searchScreenRepository.getUserInfo(userName)
        }
    }
}