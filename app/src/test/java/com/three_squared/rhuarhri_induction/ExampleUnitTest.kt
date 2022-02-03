package com.three_squared.rhuarhri_induction

import com.three_squared.rhuarhri_induction.online.ConnectionChecker
import com.three_squared.rhuarhri_induction.online.ConnectionType
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//TODO App Presentation Slide 11
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //throw UnsupportedOperationException("Not jet implemented")
        assertEquals("failing test",4, 2 + 2)

    }
}

@RunWith(MockitoJUnitRunner::class)
class MockitoTests {

    //App Presenting Testing
    /*
    This is a unit test with mockito. Mockito is used to create a version of a
    class specific for testing. For example the connection checker class
    is used to check what kind of internet connection the app has. Using
    mockito allows the test force connection checker to get a specific result.
     */

    @Mock
    private lateinit var connectionChecker: ConnectionChecker

    @Test
    fun connectionTest() {

        `when`(connectionChecker.check()).thenReturn(ConnectionType.NONE)

        val result = connectionChecker.check()

        assertEquals("check connection result", ConnectionType.NONE, result)

    }

}

