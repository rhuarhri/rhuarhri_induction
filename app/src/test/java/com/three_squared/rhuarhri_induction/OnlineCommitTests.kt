package com.three_squared.rhuarhri_induction

import com.three_squared.rhuarhri_induction.online.commits.CommitQueryHandler
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import junit.framework.Assert.assertEquals

class OnlineCommitTests {

    private val server = MockWebServer()

    private lateinit var retrofit: Retrofit

    //private val json = ""

    /*@Before
    fun setup() {

        server.enqueue(MockResponse().setBody(
            json
        ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()
    }*/

    @Test
    fun getCommitWithCompleteJson() = runBlocking {

        server.enqueue(MockResponse().setBody(
            completeJson
        ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val commitQueryHandler = CommitQueryHandler(retrofit)

        val commit = commitQueryHandler.getCommitById("userName","repository","id")

        val commitId = "d82e82bb0baa511b61a6b5b7018eaadf94980ada"
        val commitMessage = "Create README.md"
        val committerName = "rhuarhri"
        val committerId = "26746052"
        val committerAvatarURl = "https://avatars.githubusercontent.com/u/26746052?v=4"
        val repository = "repository"

        assertEquals("check commit id", commitId, commit.commitId)
        assertEquals("check commit message", commitMessage, commit.message)
        assertEquals("check committer name", committerName, commit.committerName)
        assertEquals("check committer id", committerId, commit.committerId)
        assertEquals("check commit avatar", committerAvatarURl, commit.committerAvatar)
        assertEquals("check commit repository", repository, commit.repositoryName)
    }

    @Test
    fun getCommitWithIncompleteJSON() = runBlocking {
        server.enqueue(MockResponse().setBody(
            incompleteJson
        ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val commitQueryHandler = CommitQueryHandler(retrofit)

        val commit = commitQueryHandler.getCommitById("userName","repository","id")

        val commitId = "46cf802358df9218e0beb345f9735b6360821de0"
        val commitMessage = "commit information is now stored in cache"
        val committerName = "Rhuarhri Cordon"
        /*these should be blank as the incomplete JSON value does not contain
        all the information for the committer*/
        val committerId = ""
        val committerAvatarURl = ""
        val repository = "repository"

        assertEquals("check commit id", commitId, commit.commitId)
        assertEquals("check commit message", commitMessage, commit.message)
        assertEquals("check committer name", committerName, commit.committerName)
        assertEquals("check committer id", committerId, commit.committerId)
        assertEquals("check commit avatar", committerAvatarURl, commit.committerAvatar)
        assertEquals("check commit repository", repository, commit.repositoryName)
    }

    @Test
    fun getCommitListTest() = runBlocking {
        for (json in commitList) {
            server.enqueue(MockResponse().setBody(
                json
            ))
        }

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val commitQueryHandler = CommitQueryHandler(retrofit)

        val commitList = commitQueryHandler.getCommits("userName", "repository", "8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef")

        assertEquals("check has result", 3, commitList.size)
        assertEquals("check first commit has id", "8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef", commitList.first().commitId)
        assertEquals("check second commit has id", "18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7", commitList[1].commitId)
        assertEquals("check third commit has id", "a1fb799a36db1abf006f8fd13ece338c31c5c545", commitList.last().commitId)

    }

    @Test
    fun getCommitsFromMainBranch() = runBlocking {
        server.enqueue(MockResponse().setBody(
            commitsFromMainBranch
        ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val commitQueryHandler = CommitQueryHandler(retrofit)

        val commitList = commitQueryHandler.getMainBranchCommits("user name", "repository")

        assertEquals("check has result", 2, commitList.size)
        assertEquals("check id", "eec5af39a08fa1b250c9b7a8b117ea51fe08d148", commitList.first().commitId)
    }

    @After
    fun close() {
        server.shutdown()
    }
}

/*
comes from this url
https://api.github.com/repos/rhuarhri/TrainLine/commits/d82e82bb0baa511b61a6b5b7018eaadf94980ada
 */
const val completeJson = "{\n" +
        "  \"sha\": \"d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "  \"node_id\": \"C_kwDOGP9s8NoAKGQ4MmU4MmJiMGJhYTUxMWI2MWE2YjViNzAxOGVhYWRmOTQ5ODBhZGE\",\n" +
        "  \"commit\": {\n" +
        "    \"author\": {\n" +
        "      \"name\": \"rhuarhri\",\n" +
        "      \"email\": \"rhuarhri@gmail.com\",\n" +
        "      \"date\": \"2021-10-21T19:03:12Z\"\n" +
        "    },\n" +
        "    \"committer\": {\n" +
        "      \"name\": \"GitHub\",\n" +
        "      \"email\": \"noreply@github.com\",\n" +
        "      \"date\": \"2021-10-21T19:03:12Z\"\n" +
        "    },\n" +
        "    \"message\": \"Create README.md\",\n" +
        "    \"tree\": {\n" +
        "      \"sha\": \"755bc7eb1ed73ea952a2b32bb5359152058626cb\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/trees/755bc7eb1ed73ea952a2b32bb5359152058626cb\"\n" +
        "    },\n" +
        "    \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/commits/d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "    \"comment_count\": 0,\n" +
        "    \"verification\": {\n" +
        "      \"verified\": true,\n" +
        "      \"reason\": \"valid\",\n" +
        "      \"signature\": \"-----BEGIN PGP SIGNATURE-----\\n\\nwsBcBAABCAAQBQJhcblwCRBK7hj4Ov3rIwAAj6UIAGeubNNZeGo/qX+b96B8S/aR\\nY7qDkdOXecEFKSCQnN2qzBUMGcM9Qtlq+uAPjfklwH3Mu2a3m1W8+obAjmyzIVSh\\nKREvrcEb3rDYsLNZnAeRlBKK9qkfneq5zIfe1VuR5JMsy7MVi2ghzhrSeSpDH2lE\\n6x7CxUaHMO0waV9o18BEm5gxvxhZ0KPpISWQK6eWBtvhq02kZ8A2tAipqccM7tkU\\nOUd68D/d7DJ1HeP9awQBqSUc6SIRzpRlcRTD4El4ZndUUyI2fd/1YPy+beHlDp3B\\ns+sCqyCaBaqhZvlsYcoHLmcjJmku5HRlh58qASm8tZDb+AlgMwiRypvkeZy2eQo=\\n=WD6g\\n-----END PGP SIGNATURE-----\\n\",\n" +
        "      \"payload\": \"tree 755bc7eb1ed73ea952a2b32bb5359152058626cb\\nparent 0c91a08a23b19bb16bc9c7aaa6bfd199d715815f\\nauthor rhuarhri <rhuarhri@gmail.com> 1634842992 +0100\\ncommitter GitHub <noreply@github.com> 1634842992 +0100\\n\\nCreate README.md\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "  \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "  \"comments_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/d82e82bb0baa511b61a6b5b7018eaadf94980ada/comments\",\n" +
        "  \"author\": {\n" +
        "    \"login\": \"rhuarhri\",\n" +
        "    \"id\": 26746052,\n" +
        "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "    \"gravatar_id\": \"\",\n" +
        "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "    \"type\": \"User\",\n" +
        "    \"site_admin\": false\n" +
        "  },\n" +
        "  \"committer\": {\n" +
        "    \"login\": \"rhuarhri\",\n" +
        "    \"id\": 26746052,\n" +
        "    \"node_id\": \"MDQ6VXNlcjE5ODY0NDQ3\",\n" +
        "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "    \"gravatar_id\": \"\",\n" +
        "    \"url\": \"https://api.github.com/users/web-flow\",\n" +
        "    \"html_url\": \"https://github.com/web-flow\",\n" +
        "    \"followers_url\": \"https://api.github.com/users/web-flow/followers\",\n" +
        "    \"following_url\": \"https://api.github.com/users/web-flow/following{/other_user}\",\n" +
        "    \"gists_url\": \"https://api.github.com/users/web-flow/gists{/gist_id}\",\n" +
        "    \"starred_url\": \"https://api.github.com/users/web-flow/starred{/owner}{/repo}\",\n" +
        "    \"subscriptions_url\": \"https://api.github.com/users/web-flow/subscriptions\",\n" +
        "    \"organizations_url\": \"https://api.github.com/users/web-flow/orgs\",\n" +
        "    \"repos_url\": \"https://api.github.com/users/web-flow/repos\",\n" +
        "    \"events_url\": \"https://api.github.com/users/web-flow/events{/privacy}\",\n" +
        "    \"received_events_url\": \"https://api.github.com/users/web-flow/received_events\",\n" +
        "    \"type\": \"User\",\n" +
        "    \"site_admin\": false\n" +
        "  },\n" +
        "  \"parents\": [\n" +
        "    {\n" +
        "      \"sha\": \"0c91a08a23b19bb16bc9c7aaa6bfd199d715815f\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/0c91a08a23b19bb16bc9c7aaa6bfd199d715815f\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/0c91a08a23b19bb16bc9c7aaa6bfd199d715815f\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"stats\": {\n" +
        "    \"total\": 7,\n" +
        "    \"additions\": 7,\n" +
        "    \"deletions\": 0\n" +
        "  },\n" +
        "  \"files\": [\n" +
        "    {\n" +
        "      \"sha\": \"5148fbd2eb81bd12c428ca6732c8682597785d6f\",\n" +
        "      \"filename\": \"README.md\",\n" +
        "      \"status\": \"added\",\n" +
        "      \"additions\": 7,\n" +
        "      \"deletions\": 0,\n" +
        "      \"changes\": 7,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/d82e82bb0baa511b61a6b5b7018eaadf94980ada/README.md\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/d82e82bb0baa511b61a6b5b7018eaadf94980ada/README.md\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/README.md?ref=d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "      \"patch\": \"@@ -0,0 +1,7 @@\\n+# TrainLine\\n+This is a simple tain time table app used in the 3 squared interview process\\n+\\n+As a note for anyone from 3 squared who may be looking at this\\n+Brachs\\n+Master: the master branch contains the code that I did for the test\\n+dev: the dev branch contains the an improved version of the app but was not done in the time allowed by the test\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"

/*
incompleteJson come from this url
https://api.github.com/repos/rhuarhri/rhuarhri_induction/commits/46cf802358df9218e0beb345f9735b6360821de0
The main difference between incompleteJson and completeJson is that
the author and committer values are null this is because the person
who created the commit did not have a github profile
 */
const val incompleteJson = "{\n" +
        "  \"sha\": \"46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "  \"node_id\": \"C_kwDOGZkHYtoAKDQ2Y2Y4MDIzNThkZjkyMThlMGJlYjM0NWY5NzM1YjYzNjA4MjFkZTA\",\n" +
        "  \"commit\": {\n" +
        "    \"author\": {\n" +
        "      \"name\": \"Rhuarhri Cordon\",\n" +
        "      \"email\": \"rhuarhri.cordon@3squared.com\",\n" +
        "      \"date\": \"2021-11-26T11:54:47Z\"\n" +
        "    },\n" +
        "    \"committer\": {\n" +
        "      \"name\": \"Rhuarhri Cordon\",\n" +
        "      \"email\": \"rhuarhri.cordon@3squared.com\",\n" +
        "      \"date\": \"2021-11-26T11:54:47Z\"\n" +
        "    },\n" +
        "    \"message\": \"commit information is now stored in cache\",\n" +
        "    \"tree\": {\n" +
        "      \"sha\": \"276a581911f5d432da560678e0e6a62d8d9d18d6\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/git/trees/276a581911f5d432da560678e0e6a62d8d9d18d6\"\n" +
        "    },\n" +
        "    \"url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/git/commits/46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "    \"comment_count\": 0,\n" +
        "    \"verification\": {\n" +
        "      \"verified\": false,\n" +
        "      \"reason\": \"unsigned\",\n" +
        "      \"signature\": null,\n" +
        "      \"payload\": null\n" +
        "    }\n" +
        "  },\n" +
        "  \"url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/commits/46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "  \"html_url\": \"https://github.com/rhuarhri/rhuarhri_induction/commit/46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "  \"comments_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/commits/46cf802358df9218e0beb345f9735b6360821de0/comments\",\n" +
        "  \"author\": null,\n" +
        "  \"committer\": null,\n" +
        "  \"parents\": [\n" +
        "    {\n" +
        "      \"sha\": \"13e37746c5a23b696aca8399ff6de5ff541879c4\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/commits/13e37746c5a23b696aca8399ff6de5ff541879c4\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri/rhuarhri_induction/commit/13e37746c5a23b696aca8399ff6de5ff541879c4\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"stats\": {\n" +
        "    \"total\": 131,\n" +
        "    \"additions\": 108,\n" +
        "    \"deletions\": 23\n" +
        "  },\n" +
        "  \"files\": [\n" +
        "    {\n" +
        "      \"sha\": \"112e901a8950d2a3bdf3e86e6eb4c5fe0685d500\",\n" +
        "      \"filename\": \"app/src/androidTest/java/com/three_squared/rhuarhri_induction/ExampleInstrumentedTest.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 6,\n" +
        "      \"deletions\": 4,\n" +
        "      \"changes\": 10,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/androidTest/java/com/three_squared/rhuarhri_induction/ExampleInstrumentedTest.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/androidTest/java/com/three_squared/rhuarhri_induction/ExampleInstrumentedTest.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/androidTest/java/com/three_squared/rhuarhri_induction/ExampleInstrumentedTest.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -67,9 +67,9 @@ class RealmTests {\\n \\n         dummyCommitData.addAll(\\n             listOf(\\n-                Commit(\\\"1\\\", \\\"Dave\\\", \\\"1\\\", \\\"avatar\\\", \\\"commit 1\\\"),\\n-                Commit(\\\"2\\\", \\\"Jack\\\", \\\"2\\\", \\\"avatar\\\", \\\"commit 2\\\"),\\n-                Commit(\\\"3\\\", \\\"Dave\\\", \\\"1\\\", \\\"avatar\\\", \\\"commit 3\\\")\\n+                Commit(\\\"1\\\", \\\"Dave\\\", \\\"1\\\", \\\"avatar\\\", \\\"commit 1\\\", \\\"1\\\"),\\n+                Commit(\\\"2\\\", \\\"Jack\\\", \\\"2\\\", \\\"avatar\\\", \\\"commit 2\\\", \\\"1\\\"),\\n+                Commit(\\\"3\\\", \\\"Dave\\\", \\\"1\\\", \\\"avatar\\\", \\\"commit 3\\\", \\\"1\\\")\\n             )\\n         )\\n \\n@@ -214,8 +214,9 @@ class RealmTests {\\n         val newCommitterId = \\\"3\\\"\\n         val newCommitterAvatar = \\\"Avatar 1\\\"\\n         val newCommitMessage = \\\"New commit message\\\"\\n+        val newRepositoryName = \\\"2\\\"\\n         val newCommit = Commit(existingCommit.commitId, newCommitterName, newCommitterId,\\n-            newCommitterAvatar, newCommitMessage)\\n+            newCommitterAvatar, newCommitMessage, newRepositoryName)\\n \\n         commitCache.add(newCommit)\\n \\n@@ -226,6 +227,7 @@ class RealmTests {\\n         assertEquals(\\\"check committer id\\\", newCommitterId, foundCommit.committerId)\\n         assertEquals(\\\"check committer avatar\\\", newCommitterAvatar, foundCommit.committerAvatar)\\n         assertEquals(\\\"check commit message\\\", newCommitMessage, foundCommit.message)\\n+        assertEquals(\\\"check commit repository name\\\", newRepositoryName, foundCommit.repositoryName)\\n     }\\n     //end of commit cache test\\n }\\n\\\\ No newline at end of file\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"e66a1f3979389f943b240cf9e332ffba73cb47b7\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/data/Commit.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 3,\n" +
        "      \"deletions\": 1,\n" +
        "      \"changes\": 4,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/data/Commit.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/data/Commit.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/data/Commit.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -5,4 +5,6 @@ data class Commit(\\n     val committerName : String,\\n     val committerId : String,\\n     val committerAvatar : String,\\n-    val message : String)\\n+    val message : String,\\n+    val repositoryName : String\\n+)\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"d3992e1526eb671d39138cac35fc8b32e6f943ba\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/dependency_injection/Dependencies.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 3,\n" +
        "      \"deletions\": 2,\n" +
        "      \"changes\": 5,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/dependency_injection/Dependencies.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/dependency_injection/Dependencies.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/dependency_injection/Dependencies.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -73,8 +73,9 @@ class Dependencies {\\n \\n     @Singleton\\n     @Provides\\n-    fun provideViewCommitScreenRepository() : ViewCommitScreenRepository {\\n-        return ViewCommitScreenRepository(provideQueryHandler())\\n+    fun provideViewCommitScreenRepository(@ApplicationContext app : Context) : ViewCommitScreenRepository {\\n+        return ViewCommitScreenRepository(provideQueryHandler(),\\n+            provideCommitCache(), provideConnectionChecker(app))\\n     }\\n \\n     @Singleton\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"3147f51f121922a42306e1f07dd9d6ee4cfb09b5\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/online/QueryHandler.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 2,\n" +
        "      \"deletions\": 1,\n" +
        "      \"changes\": 3,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/online/QueryHandler.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/online/QueryHandler.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/online/QueryHandler.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -119,7 +119,8 @@ class QueryHandler @Inject constructor(private val retroFit : Retrofit) {\\n                                 committerName,\\n                                 committerId,\\n                                 committerAvatar,\\n-                                commitMessage\\n+                                commitMessage,\\n+                                repositoryName\\n                             )\\n                         )\\n                     }\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"9dabffcae89374d55653025b5c255c75be66ff66\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/storage/CommitCache.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 36,\n" +
        "      \"deletions\": 4,\n" +
        "      \"changes\": 40,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/storage/CommitCache.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/storage/CommitCache.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/storage/CommitCache.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -13,7 +13,7 @@ class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheP\\n \\n         if (foundCommit == null) {\\n             val commitInternal = CommitInternal(commit.commitId, commit.committerId,\\n-                commit.committerName, commit.committerAvatar, commit.message)\\n+                commit.committerName, commit.committerAvatar, commit.message, commit.repositoryName)\\n             val realm = super.getInstance()\\n             realm.executeTransactionAwait { transaction ->\\n                 transaction.insert(commitInternal)\\n@@ -33,6 +33,7 @@ class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheP\\n                 foundCommit.committerName = commit.committerName\\n                 foundCommit.committerAvatar = commit.committerAvatar\\n                 foundCommit.message = commit.message\\n+                foundCommit.repositoryName = commit.repositoryName\\n             }\\n         }\\n     }\\n@@ -50,15 +51,46 @@ class CommitCache @Inject constructor(realmConfig : RealmConfiguration) : CacheP\\n                     foundCommit.committerName,\\n                     foundCommit.committerId,\\n                     foundCommit.committerAvatar,\\n-                    foundCommit.message\\n+                    foundCommit.message,\\n+                    foundCommit.repositoryName\\n                 )\\n             }\\n         }\\n \\n         return commit\\n     }\\n \\n-    suspend fun clear() {\\n-        TODO(\\\"Not yet implemented\\\")\\n+    suspend fun getByRepositoryName(name : String) : List<Commit> {\\n+        val commitList = mutableListOf<Commit>()\\n+\\n+        val realm = super.getInstance()\\n+        realm.executeTransactionAwait { transaction ->\\n+            val foundCommits = transaction.where(CommitInternal::class.java)\\n+                .equalTo(\\\"repositoryName\\\", name).findAll()\\n+\\n+            for (internalCommit in foundCommits) {\\n+                commitList.add(\\n+                    Commit(internalCommit.id,\\n+                        internalCommit.committerName,\\n+                        internalCommit.committerId,\\n+                        internalCommit.committerAvatar,\\n+                        internalCommit.message,\\n+                        internalCommit.repositoryName)\\n+                )\\n+            }\\n+        }\\n+\\n+        return commitList\\n+    }\\n+\\n+    suspend fun update(commits : List<Commit>) {\\n+        val expired = super.hasCacheExpired()\\n+        if (expired) {\\n+            super.deleteAll()\\n+        }\\n+\\n+        for (commit in commits) {\\n+            add(commit)\\n+        }\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"cdcdf51babc91a5aa33a6c71c32d4aa0a76b269b\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/storage/data/CommitInternal.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 3,\n" +
        "      \"deletions\": 1,\n" +
        "      \"changes\": 4,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/storage/data/CommitInternal.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/storage/data/CommitInternal.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/storage/data/CommitInternal.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -15,5 +15,7 @@ open class CommitInternal(\\n     @Required\\n     var committerAvatar : String = \\\"\\\",\\n     @Required\\n-    var message : String = \\\"\\\"\\n+    var message : String = \\\"\\\",\\n+    @Required\\n+    var repositoryName : String = \\\"\\\"\\n ) : RealmObject()\\n\\\\ No newline at end of file\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"edd06bc59fdf4df6f0875b8e68bf6c3f8cd9af8d\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/user_screen/UserScreenViewModel.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 1,\n" +
        "      \"deletions\": 1,\n" +
        "      \"changes\": 2,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/user_screen/UserScreenViewModel.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/user_screen/UserScreenViewModel.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/user_screen/UserScreenViewModel.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -28,7 +28,7 @@ class UserScreenViewModel : ViewModel() {\\n \\n             val newCommitList : MutableList<Commit> = mutableListOf()\\n             for (message in commitMessages) {\\n-                newCommitList.add(Commit(\\\"\\\", \\\"\\\", \\\"\\\", \\\"\\\", message))\\n+                newCommitList.add(Commit(\\\"\\\", \\\"\\\", \\\"\\\", \\\"\\\", message, \\\"\\\"))\\n             }\\n \\n             user.value = User(id, \\\"\\\", name, avatar, listOf())\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"165b1f0ed91378a3b6aa48401a1f9e4199b97859\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenRepository.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 53,\n" +
        "      \"deletions\": 3,\n" +
        "      \"changes\": 56,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenRepository.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenRepository.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenRepository.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -1,12 +1,62 @@\\n package com.three_squared.rhuarhri_induction.view_commit_screen\\n \\n+import androidx.lifecycle.MutableLiveData\\n import com.three_squared.rhuarhri_induction.online.QueryHandler\\n import com.three_squared.rhuarhri_induction.data.Commit\\n+import com.three_squared.rhuarhri_induction.online.ConnectionChecker\\n+import com.three_squared.rhuarhri_induction.online.ConnectionType\\n+import com.three_squared.rhuarhri_induction.storage.CommitCache\\n+import kotlinx.coroutines.Dispatchers\\n+import kotlinx.coroutines.withContext\\n import javax.inject.Inject\\n \\n-class ViewCommitScreenRepository @Inject constructor(private val queryHandler: QueryHandler){\\n+class ViewCommitScreenRepository @Inject constructor(\\n+    private val queryHandler: QueryHandler,\\n+    private val commitCache : CommitCache,\\n+    private val connectionChecker: ConnectionChecker){\\n \\n-    suspend fun getCommits(userName : String, repositoryName : String) : List<Commit> {\\n-        return queryHandler.getCommits(userName, repositoryName)\\n+    val commitList : MutableLiveData<List<Commit>> by lazy {\\n+        MutableLiveData<List<Commit>>(listOf())\\n+    }\\n+\\n+    suspend fun getCommits(userName : String, repositoryName : String) {\\n+\\n+        when(connectionChecker.check()) {\\n+            ConnectionType.STRONG -> {\\n+                //online only\\n+                val onlineCommits = queryHandler.getCommits(userName, repositoryName)\\n+\\n+                updateLiveData(onlineCommits)\\n+\\n+                commitCache.update(onlineCommits)\\n+            }\\n+            ConnectionType.WEAK -> {\\n+                //cache first\\n+                val cachedCommits = commitCache.getByRepositoryName(repositoryName)\\n+\\n+                if (cachedCommits.isNotEmpty()) {\\n+                    updateLiveData(cachedCommits)\\n+                } else {\\n+                    val onlineCommits = queryHandler.getCommits(userName, repositoryName)\\n+\\n+                    updateLiveData(onlineCommits)\\n+\\n+                    commitCache.update(onlineCommits)\\n+                }\\n+\\n+            }\\n+            ConnectionType.NONE -> {\\n+                //cache only\\n+                updateLiveData(commitCache.getByRepositoryName(repositoryName))\\n+            }\\n+        }\\n+\\n+    }\\n+\\n+\\n+    private suspend fun updateLiveData(commits : List<Commit>) {\\n+        withContext(Dispatchers.Main) {\\n+            commitList.value = commits\\n+        }\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"sha\": \"0c6b01a1f3c45fbe505ce38570f667a73d01ff3d\",\n" +
        "      \"filename\": \"app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenViewModel.kt\",\n" +
        "      \"status\": \"modified\",\n" +
        "      \"additions\": 1,\n" +
        "      \"deletions\": 6,\n" +
        "      \"changes\": 7,\n" +
        "      \"blob_url\": \"https://github.com/rhuarhri/rhuarhri_induction/blob/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenViewModel.kt\",\n" +
        "      \"raw_url\": \"https://github.com/rhuarhri/rhuarhri_induction/raw/46cf802358df9218e0beb345f9735b6360821de0/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenViewModel.kt\",\n" +
        "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/rhuarhri_induction/contents/app/src/main/java/com/three_squared/rhuarhri_induction/view_commit_screen/ViewCommitScreenViewModel.kt?ref=46cf802358df9218e0beb345f9735b6360821de0\",\n" +
        "      \"patch\": \"@@ -21,9 +21,7 @@ class ViewCommitScreenViewModel @Inject constructor(private val repo : ViewCommi\\n         )\\n     }\\n \\n-    val commitList : MutableLiveData<List<Commit>> by lazy {\\n-        MutableLiveData<List<Commit>>(listOf())\\n-    }\\n+    val commitList = repo.commitList\\n \\n     private var repositoryOwnerName = \\\"\\\"\\n     fun getRepositoryOwnerName() : String {\\n@@ -46,9 +44,6 @@ class ViewCommitScreenViewModel @Inject constructor(private val repo : ViewCommi\\n         viewModelScope.launch(Dispatchers.IO) {\\n             val commits = repo.getCommits(userName, repositoryName)\\n \\n-            withContext(Dispatchers.Main) {\\n-                commitList.value = commits\\n-            }\\n         }\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"

val commitList = listOf(
    "{\n" +
            "  \"sha\": \"8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "  \"node_id\": \"C_kwDOGP9s8NoAKDhkZGFkNThjYTNhMmQyM2FmOGUzNTIyZTRmZGNjMGIzNTAzZTA5ZWY\",\n" +
            "  \"commit\": {\n" +
            "    \"author\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-29T18:39:14Z\"\n" +
            "    },\n" +
            "    \"committer\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-29T18:39:14Z\"\n" +
            "    },\n" +
            "    \"message\": \"removal of comments and warnings\",\n" +
            "    \"tree\": {\n" +
            "      \"sha\": \"fb2a75fdf6416a96606e9332064662890780e6c3\",\n" +
            "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/trees/fb2a75fdf6416a96606e9332064662890780e6c3\"\n" +
            "    },\n" +
            "    \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/commits/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "    \"comment_count\": 0,\n" +
            "    \"verification\": {\n" +
            "      \"verified\": false,\n" +
            "      \"reason\": \"unsigned\",\n" +
            "      \"signature\": null,\n" +
            "      \"payload\": null\n" +
            "    }\n" +
            "  },\n" +
            "  \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "  \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "  \"comments_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/comments\",\n" +
            "  \"author\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"committer\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"parents\": [\n" +
            "    {\n" +
            "      \"sha\": \"18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"stats\": {\n" +
            "    \"total\": 59,\n" +
            "    \"additions\": 12,\n" +
            "    \"deletions\": 47\n" +
            "  },\n" +
            "  \"files\": [\n" +
            "    {\n" +
            "      \"sha\": \"223088125da0fc837238d9dc591887f3ff2db160\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 0,\n" +
            "      \"deletions\": 3,\n" +
            "      \"changes\": 3,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt?ref=8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "      \"patch\": \"@@ -8,7 +8,6 @@ import androidx.compose.material.*\\n import androidx.compose.material.icons.Icons\\n import androidx.compose.material.icons.filled.Refresh\\n import androidx.compose.material.icons.filled.Search\\n-import androidx.compose.runtime.getValue\\n import androidx.lifecycle.ViewModelProvider\\n import com.rhuarhri.trainline.online.Online\\n import com.rhuarhri.trainline.search_widget.SearchWidget\\n@@ -18,7 +17,6 @@ import com.rhuarhri.trainline.time_table_widget.TimeTableWidget\\n import com.rhuarhri.trainline.time_table_widget.TimeTableWidgetViewModelFactory\\n import com.rhuarhri.trainline.time_table_widget.TimetableWidgetViewModel\\n import com.rhuarhri.trainline.ui.theme.TrainLineTheme\\n-import androidx.compose.runtime.livedata.observeAsState\\n \\n \\n class MainActivity : ComponentActivity() {\\n@@ -63,7 +61,6 @@ class MainActivity : ComponentActivity() {\\n                     }, content = {Icon(Icons.Filled.Refresh, \\\"\\\")})\\n                 })\\n \\n-\\n                 SearchWidget().Widget(this, searchWidgetViewModel, onSearch = {\\n                     searchForTimeTable()\\n                 })\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"3963caf62efd9763232886fa33a9fc49dff0a3dc\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 2,\n" +
            "      \"deletions\": 4,\n" +
            "      \"changes\": 6,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt?ref=8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "      \"patch\": \"@@ -29,11 +29,9 @@ class ViewTrainTime : ComponentActivity() {\\n         val trainId = intent.getStringExtra(\\\"trainId\\\")\\n         val date = intent.getStringExtra(\\\"date\\\")\\n \\n-        viewTrainTimeViewModel = ViewModelProvider(this, ViewTrainTimeViewModelFactory(this.applicationContext))\\n+        viewTrainTimeViewModel = ViewModelProvider(this,\\n+            ViewTrainTimeViewModelFactory(this.applicationContext, trainId, date))\\n             .get(ViewTrainTimeViewModel::class.java)\\n-        if (trainId != null && date != null) {\\n-            viewTrainTimeViewModel.setup(trainId, date)\\n-        }\\n \\n         setContent {\\n             TrainLineTheme {\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"fc2524244fed51a7857ca448161ff05f93da90c1\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 0,\n" +
            "      \"deletions\": 22,\n" +
            "      \"changes\": 22,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt?ref=8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "      \"patch\": \"@@ -176,11 +176,6 @@ class SearchWidgetViewModel(context: Context) : ViewModel() {\\n     to add the most up to date versions of the information the app / widget needs\\n      */\\n \\n-    /*fun selectStation(name: String) {\\n-        val newState = SearchWidgetState(name, state.date, state.time,)\\n-        state = newState\\n-    }*/\\n-\\n     fun selectDate(day : Int, month: Int, year : Int) {\\n         val newDatePickerState = SearchWidgetDatePickerState(day, month, year)\\n         datePickerState = newDatePickerState\\n@@ -259,22 +254,5 @@ class SearchWidgetRepo(context: Context) {\\n \\n     suspend fun getPlaces() {\\n         online.getStation()\\n-        //val trainStation = online.getStation() ?: return listOf<Station>()\\n-\\n-        /*val stations = mutableListOf<Station>()\\n-        if (trainStation.member != null) {\\n-\\n-            for (stationInfo in trainStation.member) {\\n-                val name = stationInfo.name\\n-                val code = stationInfo.station_code\\n-\\n-                if (name != null && code != null) {\\n-                    val station = Station(name, code)\\n-                    stations.add(station)\\n-                }\\n-            }\\n-        }\\n-\\n-        return stations*/\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"67f5212ab910516d6eb73c081a144173c0c68dcf\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 0,\n" +
            "      \"deletions\": 13,\n" +
            "      \"changes\": 13,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt?ref=8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "      \"patch\": \"@@ -10,8 +10,6 @@ import androidx.compose.material.Text\\n import androidx.compose.runtime.Composable\\n import androidx.compose.runtime.getValue\\n import androidx.compose.runtime.livedata.observeAsState\\n-import androidx.compose.runtime.mutableStateOf\\n-import androidx.compose.runtime.setValue\\n import androidx.compose.ui.Modifier\\n import androidx.compose.ui.text.font.FontWeight\\n import androidx.compose.ui.unit.dp\\n@@ -21,9 +19,7 @@ import com.rhuarhri.trainline.ViewTrainTime\\n import com.rhuarhri.trainline.data.TimeTable\\n import com.rhuarhri.trainline.online.Online\\n import com.rhuarhri.trainline.online.time_table_data.All\\n-import kotlinx.coroutines.Dispatchers\\n import kotlinx.coroutines.launch\\n-import kotlinx.coroutines.withContext\\n \\n class TimeTableWidget {\\n \\n@@ -70,10 +66,6 @@ class TimeTableWidget {\\n     }\\n }\\n \\n-/*class TimeTableWidgetState(val timeTable : List<TimeTable>) {\\n-\\n-}*/\\n-\\n class TimeTableWidgetViewModelFactory(private val context: Context) : ViewModelProvider.Factory {\\n     /*\\n     This is a factory class. It is used to deal with the complexity of creating a view model.\\n@@ -86,13 +78,8 @@ class TimeTableWidgetViewModelFactory(private val context: Context) : ViewModelP\\n \\n class TimetableWidgetViewModel(context: Context) : ViewModel() {\\n \\n-    //var state by mutableStateOf(TimeTableWidgetState(listOf()))\\n     private val repo = TimeTableWidgetRepo(context)\\n \\n-    /*val timeTableState : LiveData<TimeTableWidgetState> = Transformations.map(repo.timeTableListLiveData) { timeTableList ->\\n-        TimeTableWidgetState(timeTableList)\\n-    }*/\\n-\\n     val timeTableSate = repo.timeTableListLiveData\\n \\n     fun search(stationName: String, date : String, time : String) {\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"236e30a6f7b0c9a2b1e25500e7d3004db1e7d3dd\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 10,\n" +
            "      \"deletions\": 5,\n" +
            "      \"changes\": 15,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt?ref=8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
            "      \"patch\": \"@@ -10,22 +10,27 @@ import com.rhuarhri.trainline.data.Stop\\n import com.rhuarhri.trainline.online.Online\\n import kotlinx.coroutines.launch\\n \\n-class ViewTrainTimeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {\\n+class ViewTrainTimeViewModelFactory(private val context: Context, private val trainId: String?,\\n+                                    private val date : String?) : ViewModelProvider.Factory {\\n     override fun <T : ViewModel?> create(modelClass: Class<T>): T {\\n-        return ViewTrainTimeViewModel(context) as T\\n+        return ViewTrainTimeViewModel(context, trainId, date) as T\\n     }\\n \\n }\\n \\n-class ViewTrainTimeViewModel(context: Context) : ViewModel() {\\n+class ViewTrainTimeViewModel(context: Context, trainId: String?, date: String?) : ViewModel() {\\n \\n     private val repo = ViewTrainTimeRepo(context)\\n     //var state by mutableStateOf(ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf()))\\n \\n     val serviceInfoState = repo.serviceInfoLiveData\\n \\n-    fun setup(trainId : String, date : String) {\\n-        if (trainId.isNotBlank() && date.isNotBlank()) {\\n+    init {\\n+        setup(trainId, date)\\n+    }\\n+\\n+    private fun setup(trainId : String?, date : String?) {\\n+        if (trainId != null && trainId.isNotBlank() && date != null && date.isNotBlank()) {\\n             viewModelScope.launch {\\n                 repo.getServiceInfo(trainId, date)\\n             }\"\n" +
            "    }\n" +
            "  ]\n" +
            "}",
    "{\n" +
            "  \"sha\": \"18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "  \"node_id\": \"C_kwDOGP9s8NoAKDE4ZGI1NGI4ZmYxZDRjMDhmMWQ2NGZiMDEyYzU4ZmNkMGUxYjc5ZDc\",\n" +
            "  \"commit\": {\n" +
            "    \"author\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-29T14:38:36Z\"\n" +
            "    },\n" +
            "    \"committer\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-29T14:38:36Z\"\n" +
            "    },\n" +
            "    \"message\": \"same functionality but now the app uses live data\",\n" +
            "    \"tree\": {\n" +
            "      \"sha\": \"962aab634d0986ff54dfe88b7c225c3f23dafe0a\",\n" +
            "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/trees/962aab634d0986ff54dfe88b7c225c3f23dafe0a\"\n" +
            "    },\n" +
            "    \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/commits/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "    \"comment_count\": 0,\n" +
            "    \"verification\": {\n" +
            "      \"verified\": false,\n" +
            "      \"reason\": \"unsigned\",\n" +
            "      \"signature\": null,\n" +
            "      \"payload\": null\n" +
            "    }\n" +
            "  },\n" +
            "  \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "  \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "  \"comments_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/comments\",\n" +
            "  \"author\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"committer\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"parents\": [\n" +
            "    {\n" +
            "      \"sha\": \"a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "      \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/a1fb799a36db1abf006f8fd13ece338c31c5c545\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"stats\": {\n" +
            "    \"total\": 347,\n" +
            "    \"additions\": 228,\n" +
            "    \"deletions\": 119\n" +
            "  },\n" +
            "  \"files\": [\n" +
            "    {\n" +
            "      \"sha\": \"2d233bce0b43b26b99a6669fa5ff800438e399d3\",\n" +
            "      \"filename\": \"app/build.gradle\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 2,\n" +
            "      \"deletions\": 0,\n" +
            "      \"changes\": 2,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/build.gradle\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/build.gradle\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/build.gradle?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -55,6 +55,8 @@ dependencies {\\n     implementation 'com.google.android.material:material:1.4.0'\\n     implementation \\\"androidx.compose.ui:ui:\\\"\\n     implementation \\\"androidx.compose.material:material:\\\"\\n+    implementation \\\"androidx.compose.runtime:runtime:\\\"\\n+    implementation \\\"androidx.compose.runtime:runtime-livedata:\\\"\\n     implementation \\\"androidx.compose.ui:ui-tooling-preview:\\\"\\n     implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.1'\\n     implementation 'androidx.activity:activity-compose:1.3.1'\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"a9a3accbe488af7865053ea4803b4b14118b76e9\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 5,\n" +
            "      \"deletions\": 2,\n" +
            "      \"changes\": 7,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/MainActivity.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -8,6 +8,7 @@ import androidx.compose.material.*\\n import androidx.compose.material.icons.Icons\\n import androidx.compose.material.icons.filled.Refresh\\n import androidx.compose.material.icons.filled.Search\\n+import androidx.compose.runtime.getValue\\n import androidx.lifecycle.ViewModelProvider\\n import com.rhuarhri.trainline.online.Online\\n import com.rhuarhri.trainline.search_widget.SearchWidget\\n@@ -17,6 +18,8 @@ import com.rhuarhri.trainline.time_table_widget.TimeTableWidget\\n import com.rhuarhri.trainline.time_table_widget.TimeTableWidgetViewModelFactory\\n import com.rhuarhri.trainline.time_table_widget.TimetableWidgetViewModel\\n import com.rhuarhri.trainline.ui.theme.TrainLineTheme\\n+import androidx.compose.runtime.livedata.observeAsState\\n+\\n \\n class MainActivity : ComponentActivity() {\\n \\n@@ -29,7 +32,7 @@ class MainActivity : ComponentActivity() {\\n \\n         searchWidgetViewModel = ViewModelProvider(this, SearchWidgetViewModelFactory(this.applicationContext))\\n             .get(SearchWidgetViewModel::class.java)\\n-        searchWidgetViewModel.setupDropDownWidget()\\n+        //searchWidgetViewModel.setupDropDownWidget()\\n \\n         timetableWidgetViewModel = ViewModelProvider(this, TimeTableWidgetViewModelFactory(this.applicationContext))\\n             .get(TimetableWidgetViewModel::class.java)\\n@@ -52,7 +55,7 @@ class MainActivity : ComponentActivity() {\\n                     },\\n                 )},\\n                     content = {\\n-                        TimeTableWidget().Widget(this, state = timetableWidgetViewModel.state)\\n+                        TimeTableWidget().Widget(this, viewModel = timetableWidgetViewModel)\\n                     },\\n                 floatingActionButton = {\\n                     FloatingActionButton(onClick = {\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"30993fb3676481ac8fc019c31fff1dbf4cfc92aa\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 21,\n" +
            "      \"deletions\": 6,\n" +
            "      \"changes\": 27,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/ViewTrainTime.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -8,9 +8,12 @@ import androidx.compose.foundation.lazy.LazyColumn\\n import androidx.compose.foundation.lazy.items\\n import androidx.compose.material.*\\n import androidx.compose.runtime.Composable\\n+import androidx.compose.runtime.getValue\\n+import androidx.compose.runtime.livedata.observeAsState\\n import androidx.compose.ui.Modifier\\n import androidx.compose.ui.unit.dp\\n import androidx.lifecycle.ViewModelProvider\\n+import com.rhuarhri.trainline.data.ServiceInfo\\n import com.rhuarhri.trainline.data.Stop\\n import com.rhuarhri.trainline.ui.theme.TrainLineTheme\\n import com.rhuarhri.trainline.veiw_train_time_screen.ViewTrainTimeViewModel\\n@@ -46,9 +49,12 @@ class ViewTrainTime : ComponentActivity() {\\n                     },\\n                     content = {\\n                         Column {\\n-                            Time(viewTrainTimeViewModel.state.date)\\n-                            Route(viewTrainTimeViewModel.state.start, viewTrainTimeViewModel.state.end)\\n-                            Stops(stops = viewTrainTimeViewModel.state.stops)\\n+                            val serviceInfo by viewTrainTimeViewModel.serviceInfoState.observeAsState(\\n+                                initial = ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf())\\n+                            )\\n+                            Time(serviceInfo.date)\\n+                            Route(serviceInfo.start, serviceInfo.end)\\n+                            Stops(stops = serviceInfo.stops)\\n                         }\\n                     },\\n                 )\\n@@ -58,14 +64,20 @@ class ViewTrainTime : ComponentActivity() {\\n \\n     @Composable\\n     fun Time(date : String) {\\n-        Row(Modifier.height(100.dp).fillMaxWidth(), Arrangement.Center) {\\n+        Row(\\n+            Modifier\\n+                .height(100.dp)\\n+                .fillMaxWidth(), Arrangement.Center) {\\n             Text(date)\\n         }\\n     }\\n \\n     @Composable\\n     fun Route(start : String, end : String) {\\n-        Column(Modifier.height(100.dp).fillMaxWidth()) {\\n+        Column(\\n+            Modifier\\n+                .height(100.dp)\\n+                .fillMaxWidth()) {\\n             Text(\\\"Route\\\")\\n             Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {\\n                 Text(start)\\n@@ -86,7 +98,10 @@ class ViewTrainTime : ComponentActivity() {\\n \\n     @Composable\\n     fun Stop(departAt : String, stationName: String) {\\n-        Row(Modifier.height(60.dp).fillMaxWidth(), Arrangement.SpaceAround) {\\n+        Row(\\n+            Modifier\\n+                .height(60.dp)\\n+                .fillMaxWidth(), Arrangement.SpaceAround) {\\n             Text(departAt)\\n             Text(stationName)\\n         }\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"a38ac591e5eeed86872c19db8e1709c4ed0c1c11\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 35,\n" +
            "      \"deletions\": 17,\n" +
            "      \"changes\": 52,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/online/Online.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -3,6 +3,7 @@ package com.rhuarhri.trainline.online\\n import android.content.Context\\n import android.net.ConnectivityManager\\n import android.net.NetworkCapabilities\\n+import androidx.lifecycle.MutableLiveData\\n import com.rhuarhri.trainline.online.time_table_data.TimeTable\\n import com.rhuarhri.trainline.online.train_service_data.Service\\n import com.rhuarhri.trainline.online.train_station_location_data.TrainStation\\n@@ -31,9 +32,13 @@ class Online(private val context: Context) {\\n             .build()\\n     }\\n \\n-    suspend fun getTimeTable(stationName: String = \\\"SHF\\\", date : String = \\\"\\\", time : String = \\\"\\\") : TimeTable? {\\n+    val currentTimeTable : MutableLiveData<TimeTable?> by lazy {\\n+        MutableLiveData<TimeTable?>()\\n+    }\\n+\\n+    suspend fun getTimeTable(stationName: String = \\\"SHF\\\", date : String = \\\"\\\", time : String = \\\"\\\") {\\n         //val url = \\\"\\\" + BASE + \\\"train/station/\\\" + stationName + \\\"/\\\" + date + \\\"/\\\" + time + \\\"/\\\"\\n-        return try {\\n+        val foundTimeTable : TimeTable? = try {\\n             val retrofitInterface = setupRetrofit().create(OnlineInterface::class.java)\\n             val response = retrofitInterface.getTimeTable(stationName,date,time).awaitResponse()\\n             if (response.isSuccessful == true) {\\n@@ -44,29 +49,40 @@ class Online(private val context: Context) {\\n         } catch (e : Exception) {\\n             null\\n         }\\n+\\n+        currentTimeTable.value = foundTimeTable\\n     }\\n \\n-    suspend fun getServiceInfo(trainId: String, date : String) : Service? {\\n-        if (trainId.isBlank() && date.isBlank()) {\\n-            return null\\n-        }\\n+    val serviceInfoLiveData : MutableLiveData<Service?> by lazy {\\n+        MutableLiveData<Service?>()\\n+    }\\n+    suspend fun getServiceInfo(trainId: String, date : String) {\\n+        val service : Service? = if (trainId.isBlank() && date.isBlank()) {\\n+            null\\n+        } else {\\n \\n-        //val url = \\\"/train/service/train_uid://\\\"\\n-        return try {\\n-            val retrofitInterface = setupRetrofit().create(OnlineInterface::class.java)\\n-            val response = retrofitInterface.getServiceInfo(trainId, date).awaitResponse()\\n-            if (response.isSuccessful == true) {\\n-                response.body()\\n-            } else {\\n+            //val url = \\\"/train/service/train_uid://\\\"\\n+            try {\\n+                val retrofitInterface = setupRetrofit().create(OnlineInterface::class.java)\\n+                val response = retrofitInterface.getServiceInfo(trainId, date).awaitResponse()\\n+                if (response.isSuccessful == true) {\\n+                    response.body()\\n+                } else {\\n+                    null\\n+                }\\n+            } catch (e: Exception) {\\n                 null\\n             }\\n-        } catch (e : Exception) {\\n-            null\\n         }\\n+\\n+        serviceInfoLiveData.value = service\\n     }\\n \\n-    suspend fun getStation() : TrainStation? {\\n-        return try {\\n+    val stationLiveData : MutableLiveData<TrainStation?> by lazy {\\n+        MutableLiveData<TrainStation?>()\\n+    }\\n+    suspend fun getStation() {\\n+        val station : TrainStation? = try {\\n         val retrofitInterface = setupRetrofit().create(OnlineInterface::class.java)\\n             val response = retrofitInterface.getPlace().awaitResponse()\\n             if (response.isSuccessful == true) {\\n@@ -77,6 +93,8 @@ class Online(private val context: Context) {\\n         } catch (e : Exception) {\\n             null\\n         }\\n+\\n+        stationLiveData.value = station\\n     }\\n \\n     companion object {\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"164e11d45ab1b653f9bd281473d61f986b56cb25\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 49,\n" +
            "      \"deletions\": 22,\n" +
            "      \"changes\": 71,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/search_widget/SearchWidget.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -12,16 +12,15 @@ import androidx.compose.foundation.layout.*\\n import androidx.compose.material.*\\n import androidx.compose.runtime.Composable\\n import androidx.compose.runtime.getValue\\n+import androidx.compose.runtime.livedata.observeAsState\\n import androidx.compose.runtime.mutableStateOf\\n import androidx.compose.runtime.setValue\\n import androidx.compose.ui.Alignment\\n import androidx.compose.ui.Modifier\\n import androidx.compose.ui.graphics.Color\\n import androidx.compose.ui.unit.dp\\n import androidx.compose.ui.unit.sp\\n-import androidx.lifecycle.ViewModel\\n-import androidx.lifecycle.ViewModelProvider\\n-import androidx.lifecycle.viewModelScope\\n+import androidx.lifecycle.*\\n import com.rhuarhri.trainline.data.Station\\n import com.rhuarhri.trainline.online.Online\\n import kotlinx.coroutines.launch\\n@@ -66,10 +65,12 @@ class SearchWidget {\\n                 .border(2.dp, MaterialTheme.colors.primary)) {\\n             Text(modifier = Modifier\\n                 .weight(1f)\\n-                .fillMaxWidth().padding(5.dp), text = title)\\n+                .fillMaxWidth()\\n+                .padding(5.dp), text = title)\\n             Text(modifier = Modifier\\n                 .weight(2f)\\n-                .fillMaxWidth().padding(5.dp), text = data, fontSize = 20.sp)\\n+                .fillMaxWidth()\\n+                .padding(5.dp), text = data, fontSize = 20.sp)\\n         }\\n     }\\n \\n@@ -80,7 +81,8 @@ class SearchWidget {\\n         })\\n         DropdownMenu(expanded = viewModel.dropDownMenuState.dropDownExpanded,\\n             onDismissRequest = { viewModel.minimiseDropDown() }) {\\n-            for (item in viewModel.dropDownMenuState.dropDownItems) {\\n+            val placesList by viewModel.placesListState.observeAsState(initial = listOf<Station>())\\n+            for (item in placesList) {\\n                 DropdownMenuItem(onClick = {viewModel.selectDropDownItem(item)}) {\\n                     Text(item.name)\\n                 }\\n@@ -125,7 +127,6 @@ class SearchWidgetState(val visible : Boolean)\\n     solution 2: don't use a data class to store state\\n     */\\n class SearchWidgetDropDownState(val dropDownExpanded : Boolean = false,\\n-                                     val dropDownItems : List<Station> = listOf(),\\n                                      val selected : Station)\\n \\n class SearchWidgetDatePickerState(val day : Int, val month : Int, val year : Int)\\n@@ -150,8 +151,13 @@ class SearchWidgetViewModel(context: Context) : ViewModel() {\\n \\n     var state by mutableStateOf(SearchWidgetState(false))\\n \\n-    var dropDownMenuState by mutableStateOf(SearchWidgetDropDownState(false,\\n-        listOf<Station>(),  Station(\\\"\\\", \\\"\\\")))\\n+    var dropDownMenuState by mutableStateOf(SearchWidgetDropDownState(false, Station(\\\"\\\", \\\"\\\")))\\n+\\n+    val placesListState = repo.places\\n+\\n+    init {\\n+        setupDropDownWidget()\\n+    }\\n \\n     private val calendar = Calendar.getInstance()\\n \\n@@ -185,34 +191,34 @@ class SearchWidgetViewModel(context: Context) : ViewModel() {\\n         timePickerState = newTimePickerState\\n     }\\n \\n-    fun setupDropDownWidget() {\\n+    private fun setupDropDownWidget() {\\n         viewModelScope.launch {\\n-            val places = repo.getPlaces()\\n-            val selected : Station = if (places.isEmpty()) {\\n+            repo.getPlaces()\\n+\\n+            val places = repo.places.value ?: listOf()\\n+            val selected : Station = if (places.isNullOrEmpty() == false) {\\n                 places.first()\\n             } else {\\n                 Station(\\\"\\\", \\\"\\\")\\n             }\\n \\n-            val newDropDownState = SearchWidgetDropDownState(false, places, selected)\\n+            val newDropDownState = SearchWidgetDropDownState(false, selected)\\n             dropDownMenuState = newDropDownState\\n         }\\n     }\\n \\n     fun selectDropDownItem(item : Station) {\\n-        val newDropDownState = SearchWidgetDropDownState(false, dropDownMenuState.dropDownItems, item)\\n+        val newDropDownState = SearchWidgetDropDownState(false, item)\\n         dropDownMenuState = newDropDownState\\n     }\\n \\n     fun expandDropDown() {\\n-        val newDropDownState = SearchWidgetDropDownState(true,\\n-            dropDownMenuState.dropDownItems, dropDownMenuState.selected)\\n+        val newDropDownState = SearchWidgetDropDownState(true, dropDownMenuState.selected)\\n         dropDownMenuState = newDropDownState\\n     }\\n \\n     fun minimiseDropDown() {\\n-        val newDropDownState = SearchWidgetDropDownState(false,\\n-            dropDownMenuState.dropDownItems, dropDownMenuState.selected)\\n+        val newDropDownState = SearchWidgetDropDownState(false, dropDownMenuState.selected)\\n         dropDownMenuState = newDropDownState\\n     }\\n \\n@@ -231,10 +237,31 @@ class SearchWidgetViewModel(context: Context) : ViewModel() {\\n class SearchWidgetRepo(context: Context) {\\n     private val online = Online(context)\\n \\n-    suspend fun getPlaces() : List<Station> {\\n-        val trainStation = online.getStation() ?: return listOf<Station>()\\n-\\n+    val places : LiveData<List<Station>> = Transformations.map(online.stationLiveData) { trainStation ->\\n         val stations = mutableListOf<Station>()\\n+        if (trainStation != null) {\\n+            if (trainStation.member != null) {\\n+\\n+                for (stationInfo in trainStation.member) {\\n+                    val name = stationInfo.name\\n+                    val code = stationInfo.station_code\\n+\\n+                    if (name != null && code != null) {\\n+                        val station = Station(name, code)\\n+                        stations.add(station)\\n+                    }\\n+                }\\n+            }\\n+        }\\n+\\n+        stations\\n+    }\\n+\\n+    suspend fun getPlaces() {\\n+        online.getStation()\\n+        //val trainStation = online.getStation() ?: return listOf<Station>()\\n+\\n+        /*val stations = mutableListOf<Station>()\\n         if (trainStation.member != null) {\\n \\n             for (stationInfo in trainStation.member) {\\n@@ -248,6 +275,6 @@ class SearchWidgetRepo(context: Context) {\\n             }\\n         }\\n \\n-        return stations\\n+        return stations*/\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"5e924cd2700fca643d477a8b7d75e0d69a6d1922\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 82,\n" +
            "      \"deletions\": 64,\n" +
            "      \"changes\": 146,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/time_table_widget/TimeTableWidget.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -9,15 +9,14 @@ import androidx.compose.foundation.lazy.items\\n import androidx.compose.material.Text\\n import androidx.compose.runtime.Composable\\n import androidx.compose.runtime.getValue\\n+import androidx.compose.runtime.livedata.observeAsState\\n import androidx.compose.runtime.mutableStateOf\\n import androidx.compose.runtime.setValue\\n import androidx.compose.ui.Modifier\\n import androidx.compose.ui.text.font.FontWeight\\n import androidx.compose.ui.unit.dp\\n import androidx.compose.ui.unit.sp\\n-import androidx.lifecycle.ViewModel\\n-import androidx.lifecycle.ViewModelProvider\\n-import androidx.lifecycle.viewModelScope\\n+import androidx.lifecycle.*\\n import com.rhuarhri.trainline.ViewTrainTime\\n import com.rhuarhri.trainline.data.TimeTable\\n import com.rhuarhri.trainline.online.Online\\n@@ -29,9 +28,10 @@ import kotlinx.coroutines.withContext\\n class TimeTableWidget {\\n \\n     @Composable\\n-    fun Widget(context : Context, state: TimeTableWidgetState) {\\n+    fun Widget(context : Context, viewModel : TimetableWidgetViewModel) {\\n+        val timeTable by viewModel.timeTableSate.observeAsState(initial = listOf())\\n         LazyColumn(Modifier.fillMaxSize(),) {\\n-            items(items = state.timeTable) { item ->\\n+            items(items = timeTable) { item ->\\n                 TimeTableItem(context = context, platform = item.platform, departAt = item.departAt,\\n                     start = item.start, destination = item.destination, trainId = item.trainId, date = item.date)\\n             }\\n@@ -44,7 +44,8 @@ class TimeTableWidget {\\n \\n         Column(modifier = Modifier\\n             .height(100.dp)\\n-            .fillMaxWidth().clickable {\\n+            .fillMaxWidth()\\n+            .clickable {\\n                 val intent = Intent(context, ViewTrainTime::class.java)\\n                 intent.putExtra(\\\"trainId\\\", trainId)\\n                 intent.putExtra(\\\"date\\\", date)\\n@@ -69,9 +70,9 @@ class TimeTableWidget {\\n     }\\n }\\n \\n-class TimeTableWidgetState(val timeTable : List<TimeTable>) {\\n+/*class TimeTableWidgetState(val timeTable : List<TimeTable>) {\\n \\n-}\\n+}*/\\n \\n class TimeTableWidgetViewModelFactory(private val context: Context) : ViewModelProvider.Factory {\\n     /*\\n@@ -85,25 +86,35 @@ class TimeTableWidgetViewModelFactory(private val context: Context) : ViewModelP\\n \\n class TimetableWidgetViewModel(context: Context) : ViewModel() {\\n \\n-    var state by mutableStateOf(TimeTableWidgetState(listOf()))\\n+    //var state by mutableStateOf(TimeTableWidgetState(listOf()))\\n     private val repo = TimeTableWidgetRepo(context)\\n \\n+    /*val timeTableState : LiveData<TimeTableWidgetState> = Transformations.map(repo.timeTableListLiveData) { timeTableList ->\\n+        TimeTableWidgetState(timeTableList)\\n+    }*/\\n+\\n+    val timeTableSate = repo.timeTableListLiveData\\n+\\n     fun search(stationName: String, date : String, time : String) {\\n \\n-        viewModelScope.launch(Dispatchers.IO) {\\n+        viewModelScope.launch {\\n+            repo.searchForTimeTable(stationName, date, time)\\n+        }\\n+\\n+        /*viewModelScope.launch(Dispatchers.IO) {\\n             val timeTable = repo.searchForTimeTable(stationName, date, time)\\n \\n             println(\\\"got result is .size}\\\")\\n-            /*\\n+            *//*\\n                I think the viewModelScope defaults to the main thread, and there\\n                is not any real benefit to running the coroutine on the IO thread\\n                however this work just fine\\n-            */\\n+            *//*\\n             withContext(Dispatchers.Main) {\\n                 val newState = TimeTableWidgetState(timeTable)\\n                 state = newState\\n             }\\n-        }\\n+        }*/\\n     }\\n \\n }\\n@@ -114,59 +125,66 @@ class TimeTableWidgetRepo(context: Context) {\\n \\n     private val online = Online(context)\\n \\n-    suspend fun searchForTimeTable(stationName: String = \\\"SHF\\\", date : String = \\\"\\\", time : String = \\\"\\\")\\n-    : List<TimeTable> {\\n-        val found = online.getTimeTable(stationName, date, time) ?: return listOf()\\n-\\n-        val timeTable = mutableListOf<TimeTable>()\\n-\\n-        val all = if (found.departures != null) {\\n-            if (found.departures.all != null) {\\n-                found.departures.all\\n-            } else {\\n-                listOf<All>()\\n-            }\\n-        } else {\\n-            listOf<All>()\\n-        }\\n-\\n-        for (item in all) {\\n-            var platform = item.platform\\n-            val departAt = item.aimed_departure_time\\n-            val start = item.origin_name\\n-            val destination = item.destination_name\\n-\\n-            val trainId = item.train_uid ?: \\\"\\\"\\n-            val trainDate = found.date ?: \\\"\\\"\\n-\\n-            /*\\n-            I think because the places.json file / functionality of the transport api is still a\\n-            working progress.\\n-            See\\n-            https://developer.transportapi.com/docs?raml=https://transportapi.com/v3/raml/transportapi.raml##uk_places_json\\n-            maybe causing the platform variable to be null\\n-            Why? because the app gets a list of train stations from the places.json file.\\n-            The code of the train station is used to find the time table of the train station, which\\n-            contains a null value for the platform.\\n-            Another possibility is that a train station with a null value platform means that it has\\n-            only one platform.\\n-            either way if the platform value is null replace with NA\\n-             */\\n-\\n-            if (platform == null) {\\n-                platform = \\\"NA\\\"\\n+    val timeTableListLiveData : LiveData<List<TimeTable>> =\\n+        Transformations.map(online.currentTimeTable) { found ->\\n+\\n+            val timeTable = mutableListOf<TimeTable>()\\n+\\n+            if (found != null) {\\n+                val all = if (found.departures != null) {\\n+                    if (found.departures.all != null) {\\n+                        found.departures.all\\n+                    } else {\\n+                        listOf<All>()\\n+                    }\\n+                } else {\\n+                    listOf<All>()\\n+                }\\n+\\n+                for (item in all) {\\n+                    var platform = item.platform\\n+                    val departAt = item.aimed_departure_time\\n+                    val start = item.origin_name\\n+                    val destination = item.destination_name\\n+\\n+                    val trainId = item.train_uid ?: \\\"\\\"\\n+                    val trainDate = found.date ?: \\\"\\\"\\n+\\n+                    /*\\n+                I think because the places.json file / functionality of the transport api is still a\\n+                working progress.\\n+                See\\n+                https://developer.transportapi.com/docs?raml=https://transportapi.com/v3/raml/transportapi.raml##uk_places_json\\n+                maybe causing the platform variable to be null\\n+                Why? because the app gets a list of train stations from the places.json file.\\n+                The code of the train station is used to find the time table of the train station, which\\n+                contains a null value for the platform.\\n+                Another possibility is that a train station with a null value platform means that it has\\n+                only one platform.\\n+                either way if the platform value is null replace with NA\\n+                */\\n+\\n+                    if (platform == null) {\\n+                        platform = \\\"NA\\\"\\n+                    }\\n+\\n+                    if (departAt != null && start != null && destination != null) {\\n+                        /*\\n+                    removing all null data and getting only the most useful data\\n+                    */\\n+                        val timeTableItem = TimeTable(\\n+                            platform = platform, departAt = departAt, start = start,\\n+                            destination = destination, trainId = trainId, date = trainDate\\n+                        )\\n+                        timeTable.add(timeTableItem)\\n+                    }\\n+                }\\n             }\\n \\n-            if (departAt != null && start != null && destination != null) {\\n-                /*\\n-                removing all null data and getting only the most useful data\\n-                 */\\n-                val timeTableItem = TimeTable(platform = platform, departAt = departAt, start = start,\\n-                    destination = destination, trainId = trainId, date = trainDate)\\n-                timeTable.add(timeTableItem)\\n-            }\\n-        }\\n+        timeTable\\n+    }\\n \\n-        return timeTable\\n+    suspend fun searchForTimeTable(stationName: String = \\\"SHF\\\", date : String = \\\"\\\", time : String = \\\"\\\") {\\n+        online.getTimeTable(stationName, date, time)\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"sha\": \"d62a3558a33930b32b467eea82926e525b26e992\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 34,\n" +
            "      \"deletions\": 8,\n" +
            "      \"changes\": 42,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/veiw_train_time_screen/ViewTrainTimeViewModel.kt?ref=18db54b8ff1d4c08f1d64fb012c58fcd0e1b79d7\",\n" +
            "      \"patch\": \"@@ -4,9 +4,7 @@ import android.content.Context\\n import androidx.compose.runtime.getValue\\n import androidx.compose.runtime.mutableStateOf\\n import androidx.compose.runtime.setValue\\n-import androidx.lifecycle.ViewModel\\n-import androidx.lifecycle.ViewModelProvider\\n-import androidx.lifecycle.viewModelScope\\n+import androidx.lifecycle.*\\n import com.rhuarhri.trainline.data.ServiceInfo\\n import com.rhuarhri.trainline.data.Stop\\n import com.rhuarhri.trainline.online.Online\\n@@ -22,12 +20,14 @@ class ViewTrainTimeViewModelFactory(private val context: Context) : ViewModelPro\\n class ViewTrainTimeViewModel(context: Context) : ViewModel() {\\n \\n     private val repo = ViewTrainTimeRepo(context)\\n-    var state by mutableStateOf(ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf()))\\n+    //var state by mutableStateOf(ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf()))\\n+\\n+    val serviceInfoState = repo.serviceInfoLiveData\\n \\n     fun setup(trainId : String, date : String) {\\n         if (trainId.isNotBlank() && date.isNotBlank()) {\\n             viewModelScope.launch {\\n-                state = repo.getServiceInfo(trainId, date)\\n+                repo.getServiceInfo(trainId, date)\\n             }\\n         }\\n     }\\n@@ -36,8 +36,34 @@ class ViewTrainTimeViewModel(context: Context) : ViewModel() {\\n class ViewTrainTimeRepo(context : Context) {\\n     private val online = Online(context)\\n \\n-    suspend fun getServiceInfo(trainId : String, date : String) : ServiceInfo {\\n-        val service = online.getServiceInfo(trainId, date) ?: return ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf())\\n+    val serviceInfoLiveData : LiveData<ServiceInfo> = Transformations.map(online.serviceInfoLiveData) { service ->\\n+\\n+        if (service != null) {\\n+\\n+            val serviceDate = service.date ?: \\\"\\\"\\n+\\n+            val serviceStart = service.origin_name ?: \\\"\\\"\\n+            val serviceEnd = service.destination_name ?: \\\"\\\"\\n+\\n+            val serviceStops = mutableListOf<Stop>()\\n+\\n+            if (service.stops != null) {\\n+                for (foundStop in service.stops) {\\n+                    if (foundStop.aimed_departure_time != null && foundStop.station_name != null) {\\n+                        val newStop = Stop(foundStop.aimed_departure_time, foundStop.station_name)\\n+                        serviceStops.add(newStop)\\n+                    }\\n+                }\\n+            }\\n+            ServiceInfo(serviceDate, serviceStart, serviceEnd, serviceStops)\\n+        } else {\\n+            ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf())\\n+        }\\n+    }\\n+\\n+    suspend fun getServiceInfo(trainId : String, date : String) {\\n+        online.getServiceInfo(trainId, date)\\n+        /*val service = online.getServiceInfo(trainId, date) ?: return ServiceInfo(\\\"\\\", \\\"\\\", \\\"\\\", listOf())\\n \\n         val serviceDate = service.date ?: \\\"\\\"\\n \\n@@ -55,6 +81,6 @@ class ViewTrainTimeRepo(context : Context) {\\n             }\\n         }\\n \\n-        return ServiceInfo(serviceDate, serviceStart, serviceEnd, serviceStops)\\n+        return ServiceInfo(serviceDate, serviceStart, serviceEnd, serviceStops)*/\\n     }\\n }\\n\\\\ No newline at end of file\"\n" +
            "    }\n" +
            "  ]\n" +
            "}",
    "{\n" +
            "  \"sha\": \"a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "  \"node_id\": \"C_kwDOGP9s8NoAKGExZmI3OTlhMzZkYjFhYmYwMDZmOGZkMTNlY2UzMzhjMzFjNWM1NDU\",\n" +
            "  \"commit\": {\n" +
            "    \"author\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-26T13:58:32Z\"\n" +
            "    },\n" +
            "    \"committer\": {\n" +
            "      \"name\": \"rhuarhri\",\n" +
            "      \"email\": \"rhuarhri@gmail.com\",\n" +
            "      \"date\": \"2021-10-26T13:58:32Z\"\n" +
            "    },\n" +
            "    \"message\": \"adding a comment\",\n" +
            "    \"tree\": {\n" +
            "      \"sha\": \"70afb23ca17e297e1182bed545c4beb4a5e405c8\",\n" +
            "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/trees/70afb23ca17e297e1182bed545c4beb4a5e405c8\"\n" +
            "    },\n" +
            "    \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/git/commits/a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "    \"comment_count\": 0,\n" +
            "    \"verification\": {\n" +
            "      \"verified\": false,\n" +
            "      \"reason\": \"unsigned\",\n" +
            "      \"signature\": null,\n" +
            "      \"payload\": null\n" +
            "    }\n" +
            "  },\n" +
            "  \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "  \"html_url\": \"https://github.com/rhuarhri/TrainLine/commit/a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "  \"comments_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/a1fb799a36db1abf006f8fd13ece338c31c5c545/comments\",\n" +
            "  \"author\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"committer\": {\n" +
            "    \"login\": \"rhuarhri\",\n" +
            "    \"id\": 26746052,\n" +
            "    \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
            "    \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
            "    \"gravatar_id\": \"\",\n" +
            "    \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
            "    \"html_url\": \"https://github.com/rhuarhri\",\n" +
            "    \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
            "    \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
            "    \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
            "    \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
            "    \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
            "    \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
            "    \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
            "    \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
            "    \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
            "    \"type\": \"User\",\n" +
            "    \"site_admin\": false\n" +
            "  },\n" +
            "  \"parents\": [\n" +
            "  ],\n" +
            "  \"stats\": {\n" +
            "    \"total\": 4,\n" +
            "    \"additions\": 4,\n" +
            "    \"deletions\": 0\n" +
            "  },\n" +
            "  \"files\": [\n" +
            "    {\n" +
            "      \"sha\": \"662225e91b3040c748369020c5c1bdc8d338cae8\",\n" +
            "      \"filename\": \"app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"status\": \"modified\",\n" +
            "      \"additions\": 4,\n" +
            "      \"deletions\": 0,\n" +
            "      \"changes\": 4,\n" +
            "      \"blob_url\": \"https://github.com/rhuarhri/TrainLine/blob/a1fb799a36db1abf006f8fd13ece338c31c5c545/app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"raw_url\": \"https://github.com/rhuarhri/TrainLine/raw/a1fb799a36db1abf006f8fd13ece338c31c5c545/app/src/main/java/com/rhuarhri/trainline/online/Online.kt\",\n" +
            "      \"contents_url\": \"https://api.github.com/repos/rhuarhri/TrainLine/contents/app/src/main/java/com/rhuarhri/trainline/online/Online.kt?ref=a1fb799a36db1abf006f8fd13ece338c31c5c545\",\n" +
            "      \"patch\": \"@@ -126,6 +126,10 @@ class Online(private val context: Context) {\\n     }\\n \\n     private fun getHttpClient() : OkHttpClient {\\n+        /*\\n+        A better solution for caching is using a room database because\\n+        it allows you to query any cached data.\\n+         */\\n         return OkHttpClient.Builder()\\n             .cache(getCache())\\n             .addNetworkInterceptor(getNetworkInterceptor())\"\n" +
            "    }\n" +
            "  ]\n" +
            "}"
)

/*
comes from
https://api.github.com/repos/rhuarhri/carbonCleanUp/commits
This is different to commitList as it is specific to the master
branch of the repository. To get branch specific commits each commit
needs to be found based on the previous commits id which is what
commitList represents
 */
const val commitsFromMainBranch = "[\n" +
        "  {\n" +
        "    \"sha\": \"eec5af39a08fa1b250c9b7a8b117ea51fe08d148\",\n" +
        "    \"node_id\": \"MDY6Q29tbWl0MjM5OTgxNzk0OmVlYzVhZjM5YTA4ZmExYjI1MGM5YjdhOGIxMTdlYTUxZmUwOGQxNDg=\",\n" +
        "    \"commit\": {\n" +
        "      \"author\": {\n" +
        "        \"name\": \"rhuarhri\",\n" +
        "        \"email\": \"rhuarhri@gmail.com\",\n" +
        "        \"date\": \"2020-04-13T09:21:06Z\"\n" +
        "      },\n" +
        "      \"committer\": {\n" +
        "        \"name\": \"rhuarhri\",\n" +
        "        \"email\": \"rhuarhri@gmail.com\",\n" +
        "        \"date\": \"2020-04-13T09:21:06Z\"\n" +
        "      },\n" +
        "      \"message\": \"finishing up the app\",\n" +
        "      \"tree\": {\n" +
        "        \"sha\": \"46f0c115f8a80e5f3659f4be8e2b6efedce55e31\",\n" +
        "        \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/trees/46f0c115f8a80e5f3659f4be8e2b6efedce55e31\"\n" +
        "      },\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/commits/eec5af39a08fa1b250c9b7a8b117ea51fe08d148\",\n" +
        "      \"comment_count\": 0,\n" +
        "      \"verification\": {\n" +
        "        \"verified\": false,\n" +
        "        \"reason\": \"unsigned\",\n" +
        "        \"signature\": null,\n" +
        "        \"payload\": null\n" +
        "      }\n" +
        "    },\n" +
        "    \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/eec5af39a08fa1b250c9b7a8b117ea51fe08d148\",\n" +
        "    \"html_url\": \"https://github.com/rhuarhri/carbonCleanUp/commit/eec5af39a08fa1b250c9b7a8b117ea51fe08d148\",\n" +
        "    \"comments_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/eec5af39a08fa1b250c9b7a8b117ea51fe08d148/comments\",\n" +
        "    \"author\": {\n" +
        "      \"login\": \"rhuarhri\",\n" +
        "      \"id\": 26746052,\n" +
        "      \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "      \"gravatar_id\": \"\",\n" +
        "      \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "      \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "      \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "      \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "      \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "      \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "      \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "      \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "      \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "      \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "      \"type\": \"User\",\n" +
        "      \"site_admin\": false\n" +
        "    },\n" +
        "    \"committer\": {\n" +
        "      \"login\": \"rhuarhri\",\n" +
        "      \"id\": 26746052,\n" +
        "      \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "      \"gravatar_id\": \"\",\n" +
        "      \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "      \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "      \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "      \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "      \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "      \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "      \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "      \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "      \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "      \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "      \"type\": \"User\",\n" +
        "      \"site_admin\": false\n" +
        "    },\n" +
        "    \"parents\": [\n" +
        "      {\n" +
        "        \"sha\": \"9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "        \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "        \"html_url\": \"https://github.com/rhuarhri/carbonCleanUp/commit/9b30e3a612a1161cc10813b561206d9acaf7e585\"\n" +
        "      }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    \"sha\": \"9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "    \"node_id\": \"MDY6Q29tbWl0MjM5OTgxNzk0OjliMzBlM2E2MTJhMTE2MWNjMTA4MTNiNTYxMjA2ZDlhY2FmN2U1ODU=\",\n" +
        "    \"commit\": {\n" +
        "      \"author\": {\n" +
        "        \"name\": \"rhuarhri\",\n" +
        "        \"email\": \"rhuarhri@gmail.com\",\n" +
        "        \"date\": \"2020-03-24T20:15:55Z\"\n" +
        "      },\n" +
        "      \"committer\": {\n" +
        "        \"name\": \"rhuarhri\",\n" +
        "        \"email\": \"rhuarhri@gmail.com\",\n" +
        "        \"date\": \"2020-03-24T20:15:55Z\"\n" +
        "      },\n" +
        "      \"message\": \"Travel screen working. General fixing of the UI required.\",\n" +
        "      \"tree\": {\n" +
        "        \"sha\": \"8a4e87e2ca9b1e9e021d02932f20f578c2dc07a4\",\n" +
        "        \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/trees/8a4e87e2ca9b1e9e021d02932f20f578c2dc07a4\"\n" +
        "      },\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/commits/9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "      \"comment_count\": 0,\n" +
        "      \"verification\": {\n" +
        "        \"verified\": false,\n" +
        "        \"reason\": \"unsigned\",\n" +
        "        \"signature\": null,\n" +
        "        \"payload\": null\n" +
        "      }\n" +
        "    },\n" +
        "    \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "    \"html_url\": \"https://github.com/rhuarhri/carbonCleanUp/commit/9b30e3a612a1161cc10813b561206d9acaf7e585\",\n" +
        "    \"comments_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/9b30e3a612a1161cc10813b561206d9acaf7e585/comments\",\n" +
        "    \"author\": {\n" +
        "      \"login\": \"rhuarhri\",\n" +
        "      \"id\": 26746052,\n" +
        "      \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "      \"gravatar_id\": \"\",\n" +
        "      \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "      \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "      \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "      \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "      \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "      \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "      \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "      \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "      \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "      \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "      \"type\": \"User\",\n" +
        "      \"site_admin\": false\n" +
        "    },\n" +
        "    \"committer\": {\n" +
        "      \"login\": \"rhuarhri\",\n" +
        "      \"id\": 26746052,\n" +
        "      \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "      \"gravatar_id\": \"\",\n" +
        "      \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "      \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "      \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "      \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "      \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "      \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "      \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "      \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "      \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "      \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "      \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "      \"type\": \"User\",\n" +
        "      \"site_admin\": false\n" +
        "    },\n" +
        "    \"parents\": [\n" +
        "      {\n" +
        "        \"sha\": \"5980318cd8ebf586906dfcc3136a38a49bece9f8\",\n" +
        "        \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits/5980318cd8ebf586906dfcc3136a38a49bece9f8\",\n" +
        "        \"html_url\": \"https://github.com/rhuarhri/carbonCleanUp/commit/5980318cd8ebf586906dfcc3136a38a49bece9f8\"\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "]"