package com.three_squared.rhuarhri_induction.view_commit_screen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.three_squared.rhuarhri_induction.data.Repository

class ViewCommitScreenViewModel : ViewModel() {

    val repository : MutableLiveData<Repository> by lazy {
        MutableLiveData<Repository>(
            Repository("", "", "", "")
        )
    }

    fun setup(bundle: Bundle?) {
        if (bundle != null) {
            val id = bundle.getString("id") ?: ""
            val name = bundle.getString("name") ?: ""
            val visibility = bundle.getString("visibility") ?: ""
            val description = bundle.getString("description") ?: ""

            repository.value = Repository(id, name, visibility, description)
        }
    }
}