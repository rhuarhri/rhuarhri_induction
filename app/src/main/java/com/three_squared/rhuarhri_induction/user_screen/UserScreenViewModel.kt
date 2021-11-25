package com.three_squared.rhuarhri_induction.user_screen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.User

class UserScreenViewModel : ViewModel() {

    val user : MutableLiveData<User> by lazy {
        MutableLiveData<User>(
            User("", "", "", "", listOf())
        )
    }

    val commitList : MutableLiveData<List<Commit>> by lazy {
        MutableLiveData<List<Commit>>(listOf())
    }

    fun setup(bundle: Bundle?) {
        if (bundle != null) {
            val id = bundle.getString("id") ?: ""
            val name = bundle.getString("name") ?: ""
            val avatar = bundle.getString("avatar") ?: ""

            val commitMessages : List<String> = bundle.getStringArrayList("commitMessages") ?: listOf()

            val newCommitList : MutableList<Commit> = mutableListOf()
            for (message in commitMessages) {
                newCommitList.add(Commit("", "", "", "", message))
            }

            user.value = User(id, "", name, avatar, listOf())

            commitList.value = newCommitList
        }
    }
}