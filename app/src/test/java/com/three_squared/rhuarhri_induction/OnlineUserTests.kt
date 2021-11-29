package com.three_squared.rhuarhri_induction

import com.three_squared.rhuarhri_induction.online.users.UserQueryHandler
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OnlineUserTests {

    private val server = MockWebServer()

    private lateinit var retrofit: Retrofit

    @Test
    fun getUserTest() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                userJson
            ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val userQueryHandler = UserQueryHandler(retrofit)

        val user = userQueryHandler.getUser("test")

        assertEquals("check id", "26746052", user.id)
        assertEquals("check name", "rhuarhri", user.name)
        assertEquals("check avatar url", "https://avatars.githubusercontent.com/u/26746052?v=4", user.avatar)
        assertEquals("check repository url", "https://api.github.com/users/rhuarhri/repos", user.repoListURL)
        assertEquals("check repository list", true, user.repositoryList.isEmpty())
    }

    @Test
    fun getUserNoJsonTest() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                ""
            ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val userQueryHandler = UserQueryHandler(retrofit)

        val user = userQueryHandler.getUser("test")

        assertEquals("check id", "", user.id)
        assertEquals("check name", "", user.name)
        assertEquals("check avatar url", "", user.avatar)
        assertEquals("check repository url", "", user.repoListURL)
        assertEquals("check repository list", true, user.repositoryList.isEmpty())
    }

    @After
    fun close() {
        server.shutdown()
    }
}

const val userJson = "{\n" +
        "  \"login\": \"rhuarhri\",\n" +
        "  \"id\": 26746052,\n" +
        "  \"node_id\": \"MDQ6VXNlcjI2NzQ2MDUy\",\n" +
        "  \"avatar_url\": \"https://avatars.githubusercontent.com/u/26746052?v=4\",\n" +
        "  \"gravatar_id\": \"\",\n" +
        "  \"url\": \"https://api.github.com/users/rhuarhri\",\n" +
        "  \"html_url\": \"https://github.com/rhuarhri\",\n" +
        "  \"followers_url\": \"https://api.github.com/users/rhuarhri/followers\",\n" +
        "  \"following_url\": \"https://api.github.com/users/rhuarhri/following{/other_user}\",\n" +
        "  \"gists_url\": \"https://api.github.com/users/rhuarhri/gists{/gist_id}\",\n" +
        "  \"starred_url\": \"https://api.github.com/users/rhuarhri/starred{/owner}{/repo}\",\n" +
        "  \"subscriptions_url\": \"https://api.github.com/users/rhuarhri/subscriptions\",\n" +
        "  \"organizations_url\": \"https://api.github.com/users/rhuarhri/orgs\",\n" +
        "  \"repos_url\": \"https://api.github.com/users/rhuarhri/repos\",\n" +
        "  \"events_url\": \"https://api.github.com/users/rhuarhri/events{/privacy}\",\n" +
        "  \"received_events_url\": \"https://api.github.com/users/rhuarhri/received_events\",\n" +
        "  \"type\": \"User\",\n" +
        "  \"site_admin\": false,\n" +
        "  \"name\": null,\n" +
        "  \"company\": null,\n" +
        "  \"blog\": \"\",\n" +
        "  \"location\": null,\n" +
        "  \"email\": null,\n" +
        "  \"hireable\": null,\n" +
        "  \"bio\": null,\n" +
        "  \"twitter_username\": null,\n" +
        "  \"public_repos\": 17,\n" +
        "  \"public_gists\": 0,\n" +
        "  \"followers\": 3,\n" +
        "  \"following\": 0,\n" +
        "  \"created_at\": \"2017-03-28T16:17:31Z\",\n" +
        "  \"updated_at\": \"2021-10-20T15:04:55Z\"\n" +
        "}"