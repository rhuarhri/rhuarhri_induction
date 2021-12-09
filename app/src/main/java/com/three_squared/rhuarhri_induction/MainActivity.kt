package com.three_squared.rhuarhri_induction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.realm.Realm

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /*TODO App Presentation 1 Main activity
    You can consider an activity as a screen displayed by the app.
    But this activity is empty. This is because of the single activity
    architecture.

    The way this architecture works is that an app ony has one screen. In this
    case it is the MainActivity. But in order to give the app multiple screens
    fragments are used instead. A fragment is a chunk of UI. But in this app
    they are made to look like entire screens and as the user navigates they are
    swapped in and out of the activity.

    The main benefit of this architecture is that sharing information between screens
    is easier, as the each fragment has access to all information in the activity.
    If I was to do this project again I would try to capitalize on this advantage
    by storing more information in the MainActivity.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
    }
}