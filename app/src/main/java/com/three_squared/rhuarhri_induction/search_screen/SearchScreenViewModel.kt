package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.online.RetroFitHandler
import kotlinx.coroutines.launch

class SearchScreenViewModel : ViewModel() {

    val refreshed = "refreshed 00:00:00"

    val searchListResult : List<String> = listOf("test 1", "test 2", "test 3", "test 4")

    fun searchGithub() {
        viewModelScope.launch {
            RetroFitHandler().getUserInfo()
        }
    }
}