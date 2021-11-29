package com.three_squared.rhuarhri_induction

import com.three_squared.rhuarhri_induction.online.repositories.RepositoryQueryHandler
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OnlineRepositoryTests {

    private val server = MockWebServer()

    private lateinit var retrofit: Retrofit

    @Test
    fun getRepositoryTest() = runBlocking {
        server.enqueue(
            MockResponse().setBody(
                repositoryJson
            ))

        server.start()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()

        val repositoryQueryHandler = RepositoryQueryHandler(retrofit)

        val repositories = repositoryQueryHandler.getRepositories("user name")

        assertEquals("check has result", 1, repositories.size)

        assertEquals("check id", "239981794", repositories.first().id)
        assertEquals("check name", "carbonCleanUp", repositories.first().name)
        assertEquals("check visibility", "public", repositories.first().visibility)
        assertEquals("check description", "carbon offset app for iphone programming module", repositories.first().description)
    }

    @After
    fun close() {
        server.shutdown()
    }
}

/*
comes from
https://api.github.com/repos/rhuarhri/rhuarhri_induction
 */
const val repositoryJson = "[\n" +
        "  {\n" +
        "    \"id\": 239981794,\n" +
        "    \"node_id\": \"MDEwOlJlcG9zaXRvcnkyMzk5ODE3OTQ=\",\n" +
        "    \"name\": \"carbonCleanUp\",\n" +
        "    \"full_name\": \"rhuarhri/carbonCleanUp\",\n" +
        "    \"private\": false,\n" +
        "    \"owner\": {\n" +
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
        "    \"html_url\": \"https://github.com/rhuarhri/carbonCleanUp\",\n" +
        "    \"description\": \"carbon offset app for iphone programming module\",\n" +
        "    \"fork\": false,\n" +
        "    \"url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp\",\n" +
        "    \"forks_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/forks\",\n" +
        "    \"keys_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/keys{/key_id}\",\n" +
        "    \"collaborators_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/collaborators{/collaborator}\",\n" +
        "    \"teams_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/teams\",\n" +
        "    \"hooks_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/hooks\",\n" +
        "    \"issue_events_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/issues/events{/number}\",\n" +
        "    \"events_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/events\",\n" +
        "    \"assignees_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/assignees{/user}\",\n" +
        "    \"branches_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/branches{/branch}\",\n" +
        "    \"tags_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/tags\",\n" +
        "    \"blobs_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/blobs{/sha}\",\n" +
        "    \"git_tags_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/tags{/sha}\",\n" +
        "    \"git_refs_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/refs{/sha}\",\n" +
        "    \"trees_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/trees{/sha}\",\n" +
        "    \"statuses_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/statuses/{sha}\",\n" +
        "    \"languages_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/languages\",\n" +
        "    \"stargazers_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/stargazers\",\n" +
        "    \"contributors_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/contributors\",\n" +
        "    \"subscribers_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/subscribers\",\n" +
        "    \"subscription_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/subscription\",\n" +
        "    \"commits_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/commits{/sha}\",\n" +
        "    \"git_commits_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/git/commits{/sha}\",\n" +
        "    \"comments_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/comments{/number}\",\n" +
        "    \"issue_comment_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/issues/comments{/number}\",\n" +
        "    \"contents_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/contents/{+path}\",\n" +
        "    \"compare_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/compare/{base}...{head}\",\n" +
        "    \"merges_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/merges\",\n" +
        "    \"archive_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/{archive_format}{/ref}\",\n" +
        "    \"downloads_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/downloads\",\n" +
        "    \"issues_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/issues{/number}\",\n" +
        "    \"pulls_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/pulls{/number}\",\n" +
        "    \"milestones_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/milestones{/number}\",\n" +
        "    \"notifications_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/notifications{?since,all,participating}\",\n" +
        "    \"labels_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/labels{/name}\",\n" +
        "    \"releases_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/releases{/id}\",\n" +
        "    \"deployments_url\": \"https://api.github.com/repos/rhuarhri/carbonCleanUp/deployments\",\n" +
        "    \"created_at\": \"2020-02-12T10:10:51Z\",\n" +
        "    \"updated_at\": \"2020-04-13T09:21:16Z\",\n" +
        "    \"pushed_at\": \"2020-04-13T09:21:13Z\",\n" +
        "    \"git_url\": \"git://github.com/rhuarhri/carbonCleanUp.git\",\n" +
        "    \"ssh_url\": \"git@github.com:rhuarhri/carbonCleanUp.git\",\n" +
        "    \"clone_url\": \"https://github.com/rhuarhri/carbonCleanUp.git\",\n" +
        "    \"svn_url\": \"https://github.com/rhuarhri/carbonCleanUp\",\n" +
        "    \"homepage\": null,\n" +
        "    \"size\": 1736,\n" +
        "    \"stargazers_count\": 0,\n" +
        "    \"watchers_count\": 0,\n" +
        "    \"language\": \"Swift\",\n" +
        "    \"has_issues\": true,\n" +
        "    \"has_projects\": true,\n" +
        "    \"has_downloads\": true,\n" +
        "    \"has_wiki\": true,\n" +
        "    \"has_pages\": false,\n" +
        "    \"forks_count\": 0,\n" +
        "    \"mirror_url\": null,\n" +
        "    \"archived\": false,\n" +
        "    \"disabled\": false,\n" +
        "    \"open_issues_count\": 0,\n" +
        "    \"license\": null,\n" +
        "    \"allow_forking\": true,\n" +
        "    \"is_template\": false,\n" +
        "    \"topics\": [\n" +
        "\n" +
        "    ],\n" +
        "    \"visibility\": \"public\",\n" +
        "    \"forks\": 0,\n" +
        "    \"open_issues\": 0,\n" +
        "    \"watchers\": 0,\n" +
        "    \"default_branch\": \"master\"\n" +
        "  }\n" +
        "]"