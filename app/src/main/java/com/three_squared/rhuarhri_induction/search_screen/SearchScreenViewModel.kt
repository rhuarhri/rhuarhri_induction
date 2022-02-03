package com.three_squared.rhuarhri_induction.search_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.three_squared.rhuarhri_induction.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val searchScreenRepository: SearchScreenRepository): ViewModel() {

    val loading : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun searchForUser(userName : String, updateUser :  (user : User) -> Unit) {
        if (userName.isBlank()) {
            updateUser.invoke(User("", "", "", "", listOf()))
        } else {
            loading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val foundUser = searchScreenRepository.getUserInfo(userName)

                withContext(Dispatchers.Main) {
                    updateUser.invoke(foundUser)

                    loading.value = false
                }
            }
        }
    }

}