package com.three_squared.rhuarhri_induction

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.three_squared.rhuarhri_induction.data.User
import com.three_squared.rhuarhri_induction.storage.CacheParent
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

    private val dummyData = mutableListOf<User>()
    //private lateinit var appContext : Context
    private var testConfig : RealmConfiguration? = null

    @Before
    fun setupDummyData() {
        dummyData.addAll(
            listOf(
                User("1", "repoURL", "Sam", "avatar"),
                User("2", "repoURL", "Jack", "avatar"),
                User("3", "repoURL", "Jim", "avatar")
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

        for (data in dummyData) {
            userCache.add(data)
        }

        val expectedName = "Sam"

        val foundUsers = userCache.getByName(expectedName)

        assertEquals("check result size", 1, foundUsers.size)

        val foundUser = foundUsers.first()

        assertEquals("check name in result", expectedName, foundUser.name)
    }

    //end of user cache tests

}