package com.three_squared.rhuarhri_induction

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OnlineBranchTest {

    private val server = MockWebServer()

    private lateinit var retrofit: Retrofit

    @Test
    fun getBranchesTest() = runBlocking {

        server.enqueue(
            MockResponse().setBody(
            branchList
        ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

    }

    @After
    fun close() {
        server.shutdown()
    }
}

/*
Comes from
https://api.github.com/repos/rhuarhri/TrainLine/branches
 */
const val branchList : String = "[\n" +
        "  {\n" +
        "    \"name\": \"dev\",\n" +
        "    \"commit\": {\n" +
        "      \"sha\": \"8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/8ddad58ca3a2d23af8e3522e4fdcc0b3503e09ef\"\n" +
        "    },\n" +
        "    \"protected\": false\n" +
        "  },\n" +
        "  {\n" +
        "    \"name\": \"master\",\n" +
        "    \"commit\": {\n" +
        "      \"sha\": \"d82e82bb0baa511b61a6b5b7018eaadf94980ada\",\n" +
        "      \"url\": \"https://api.github.com/repos/rhuarhri/TrainLine/commits/d82e82bb0baa511b61a6b5b7018eaadf94980ada\"\n" +
        "    },\n" +
        "    \"protected\": false\n" +
        "  }\n" +
        "]"