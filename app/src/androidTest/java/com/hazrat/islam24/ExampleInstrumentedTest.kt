package com.hazrat.islam24

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

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
        assertEquals("com.hazrat.islam24", appContext.packageName)
    }

    /**
     * Sends a non-fatal test exception to Firebase Crashlytics.
     * This will appear in your Firebase Crashlytics console under the "Non-fatal" filter.
     */
    @Test
    fun testCrashlyticsNonFatal() {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("test_runner", "AndroidJUnit4")
        crashlytics.setCustomKey("environment", "androidTest")
        crashlytics.log("Testing Firebase Crashlytics from ExampleInstrumentedTest")
        
        // Log a non-fatal exception to Firebase Crashlytics
        crashlytics.recordException(
            RuntimeException("Test Non-Fatal: Firebase Crashlytics is working properly!")
        )
        
        // Allow background thread to transmit the report before test execution ends
        Thread.sleep(3000)
    }

    /**
     * Triggers a fatal uncaught crash on a separate thread.
     *
     * NOTE: For this crash report to reach Firebase Crashlytics:
     * 1. The thread crashes and Crashlytics logs it to local storage.
     * 2. You MUST RESTART the app afterward, because Crashlytics transmits
     *    crash reports to Firebase servers on the NEXT application launch.
     */
    @Test
    fun testCrashlyticsFatal() {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log("Fatal crash triggered via testCrashlyticsFatal()")
        
        // Thrown on a background thread so JUnit's runner does not intercept it
        Thread {
            throw RuntimeException("Test Fatal Crash: Firebase Crashlytics unhandled exception test!")
        }.start()
        
        // Keep the test thread alive momentarily so the uncaught exception handler can run
        Thread.sleep(2000)
    }
}