package com.three_squared.rhuarhri_induction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User

class MainActivityViewModel : ViewModel() {

    /*
    This is used to share data between screens. All the fragments
    exists inside the main activity and the main activity has
    access to this view model. As a result all fragments (the app's screens)
    have access to this view model an in turn the data inside it.
     */

    val userInfo : MutableLiveData<User> = MutableLiveData<User>(User("", "", "", "", listOf()))

    val repositoryList : MutableLiveData<List<Repository>> = MutableLiveData<List<Repository>>(listOf())

    val commitList : MutableLiveData<List<Commit>> = MutableLiveData<List<Commit>>(listOf())

    val errorMessage : MutableLiveData<String> = MutableLiveData<String>("")

    fun setUserInfo(newUser : User) {
        println("new user name is ${newUser.name}")
        println("new user id is ${newUser.id}")
        userInfo.value = newUser

        repositoryList.value = newUser.repositoryList

        when {
            newUser.id.isBlank() -> {
                errorMessage.value = "No user found "
            }
            newUser.repositoryList.isEmpty() -> {
                errorMessage.value = "No repositories found"
            }
            else -> {
                errorMessage.value = ""
            }
        }
    }

    fun setCommitList(newCommitList : List<Commit>) {
        commitList.value = newCommitList

        if (newCommitList.isEmpty()) {
            errorMessage.value = "No commits found"
        } else {
            errorMessage.value = ""
        }
    }

}