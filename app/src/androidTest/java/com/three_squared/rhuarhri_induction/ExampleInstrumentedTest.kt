package com.three_squared.rhuarhri_induction

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.three_squared.rhuarhri_induction.data.Commit
import com.three_squared.rhuarhri_induction.data.Repository
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.CacheParent
import com.three_squared.rhuarhri_induction.storage.CommitCache
import com.three_squared.rhuarhri_induction.storage.RepositoryCache
import com.three_squared.rhuarhri_induction.storage.UserCache
import com.three_squared.rhuarhri_induction.storage.data.CacheHistory
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.three_squared.rhuarhri_induction", appContext.packageName)
    }

}

@RunWith(AndroidJUnit4::class)
class RealmTests {

    private val dummyUserData = mutableListOf<User>()
    private val dummyRepositoryData = mutableListOf<Repository>()
    private val dummyCommitData = mutableListOf<Commit>()

    private var testConfig : RealmConfiguration? = null

    @Before
    fun setupDummyData() {
        dummyUserData.addAll(
            listOf(
                User("1", "repoURL", "Sam", "avatar", listOf()),
                User("2", "repoURL", "Jack", "avatar", listOf()),
                User("3", "repoURL", "Jim", "avatar", listOf())
            )
        )

        dummyRepositoryData.addAll(
            listOf(
                Repository("1", "repo 1", "public", "description"),
                Repository("2", "repo 2", "public", "description"),
                Repository("3", "repo 3", "public", "description"),
            )
        )

        dummyCommitData.addAll(
            listOf(
                Commit("1", "Dave", "1", "avatar", "commit 1"),
                Commit("2", "Jack", "2", "avatar", "commit 2"),
                Commit("3", "Dave", "1", "avatar", "commit 3")
            )
        )

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        Realm.init(appContext)

        testConfig = RealmConfiguration.Builder().inMemory().name("test-realm").build()
    }

    //parent / abstract cache tests
    @Test
    fun checkIfCacheValidTestOnEmptyDatabase() = runBlocking {
        //this should check the age of the cached data
        val cacheParent = CacheParent<User>(testConfig!!)

        val result = cacheParent.hasCacheExpired()

        //since that the cache and by extension realm does not contain any
        //data then the cache has not expired

        assertEquals("Check cache has not expired test on empty data base", false, result)
    }

    @Test
    fun checkIfCacheValid() = runBlocking {

        val oldDate = LocalDate.now().minusDays(14)

        val cacheParent = CacheParent<User>(testConfig!!)

        val realm = cacheParent.getInstance()

        //oldDate was added to the database to represent when the cache was added
        realm.executeTransactionAwait { transaction ->
            val newCacheHistory = CacheHistory(time = oldDate.toEpochDay())
            transaction.insert(newCacheHistory)
        }

        val result = cacheParent.hasCacheExpired()
        //since oldDate is 14 days behind the current data the cache will have expired

        assertEquals("check cache expired with old date", true, result)
    }

    //user cache tests
    @Test
    fun testSearch() = runBlocking {

        val userCache = UserCache(testConfig!!)

        for (data in dummyUserData) {
            userCache.add(data)
        }

        val expectedName = "Sam"

        val foundUsers = userCache.getByName(expectedName)

        assertEquals("check result size", 1, foundUsers.size)

        val foundUser = foundUsers.first()

        assertEquals("check name in result", expectedName, foundUser.name)
    }

    @Test
    fun updateUser() = runBlocking {
        //this check if the up to date information of the user
        //replaces the existing information stored in the database

        val userCache = UserCache(testConfig!!)

        val existingUser = dummyUserData.first()

        userCache.add(existingUser)

        val newName = "Jack"
        val newRepoURL = "repoURL1"
        val newAvatar = "avatar1"
        val newRepoList = listOf(
            Repository("1", "project 1", "public", "description")
        )
        val newUpdatedUser = User(existingUser.id, newRepoURL,
            newName, newAvatar, newRepoList)

        userCache.add(newUpdatedUser)

        val foundUser = userCache.get(existingUser.id)

        //they are all related to the same test so that is why these asserts are here
        assertNotEquals("check has result", null, foundUser)
        assertEquals("check name change", newName, foundUser!!.name)
        assertEquals("check repoURL change", newRepoURL, foundUser.repoListURL)
        assertEquals("check avatar change", newAvatar, foundUser.avatar)
        assertEquals("check repo list not empty", false, foundUser.repositoryList.isEmpty())
    }

    //end of user cache tests

    //start of repository cache test
    @Test
    fun updateRepositoryTest() = runBlocking {
        val repoCache = RepositoryCache(testConfig!!)

        val existingRepository = dummyRepositoryData.first()

        repoCache.add(existingRepository)

        val newName = "repo 2"
        val newDescription = "description 2"
        val newVisibility = "Private"

        val newRepository = Repository(existingRepository.id, newName, newVisibility, newDescription)

        repoCache.add(newRepository)

        val foundRepository = repoCache.getById(existingRepository.id)

        assertNotEquals("check is result exists", null, foundRepository)
        assertEquals("check name change", newName, foundRepository!!.name)
        assertEquals("check visibility change", newVisibility, foundRepository.visibility)
        assertEquals("check description changed", newDescription, foundRepository.description)
    }

    @Test
    fun getRepositoriesTest() = runBlocking {

    }

    //end of repository cache test
    //start of commit cache test
    @Test
    fun updateCommitTest() = runBlocking {
        val commitCache = CommitCache(testConfig!!)

        val existingCommit = dummyCommitData.first()

        commitCache.add(existingCommit)

        val newCommitterName = "Tom"
        val newCommitterId = "3"
        val newCommitterAvatar = "Avatar 1"
        val newCommitMessage = "New commit message"
        val newCommit = Commit(existingCommit.commitId, newCommitterName, newCommitterId,
            newCommitterAvatar, newCommitMessage)

        commitCache.add(newCommit)

        val foundCommit = commitCache.getById(existingCommit.commitId)

        assertNotEquals("check result exists", null, foundCommit)
        assertEquals("check committer name", newCommitterName, foundCommit!!.committerName)
        assertEquals("check committer id", newCommitterId, foundCommit.committerId)
        assertEquals("check committer avatar", newCommitterAvatar, foundCommit.committerAvatar)
        assertEquals("check commit message", newCommitMessage, foundCommit.message)
    }
    //end of commit cache test
}