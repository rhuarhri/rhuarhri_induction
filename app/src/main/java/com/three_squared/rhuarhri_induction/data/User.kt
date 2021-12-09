package com.three_squared.rhuarhri_induction.data

/*  App Presentation data classes
In kotlin data classes only store information.
Each of the data classes (i.e. User, Commit and Repository) are used
through out the app expect for the online and database sections of the
app. These sections have there own special versions of these data classes which
have additional functionality for example converting JSON into a class.
 */

data class User(
    val id : String,
    val repoListURL : String,
    val name : String,
    val avatar : String,
    val repositoryList : List<Repository>
)
