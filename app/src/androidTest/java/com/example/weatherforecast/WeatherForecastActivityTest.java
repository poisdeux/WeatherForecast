package com.example.weatherforecast;

import android.app.Activity;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.weatherforecast.database.WeatherForecastDatabase;
import com.example.weatherforecast.testHelpers.FlakyTest;
import com.example.weatherforecast.testHelpers.Utils;
import com.example.weatherforecast.utils.DateUtils;
import com.example.weatherforecast.utils.TestData;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * These tests must be run using the instrumentationTestDebug build variant
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class WeatherForecastActivityTest {

    private MockWebServer server;

    @Rule
    public ActivityTestRule<WeatherForecastActivity> mActivityRule =
            new ActivityTestRule<>(WeatherForecastActivity.class);

    @Before
    public void setUp() throws Exception {
        Utils.disableAnimations(mActivityRule.getActivity());

        server = new MockWebServer();
        server.start();
        mActivityRule.getActivity().setForecastUrl(server.url("").toString());
    }

    @After
    public void tearDown() throws Exception {
        Utils.enableAnimations(mActivityRule.getActivity());
        resetDatabase();
    }

    @Test
    public void showInstructionsWhenNoDataToShowTest() {
        onView(withId(R.id.emptyview)).check(matches(
                withText(mActivityRule.getActivity().getString(R.string.swipe_down_to_refresh))));
    }

    @Test
    public void swipeDownToRefreshTest() throws Exception {
        MockResponse response = new MockResponse();
        response.setBody(TestData.getJsonResponse());
        server.enqueue(response);

        onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown());

        for( WeatherData weatherData : TestData.getExpectedForecast().values() ) {
            onView(allOf(withText(DateUtils.toDateString(weatherData.date)))).check(matches(isDisplayed()));
        }
    }


    @Test
    public void refreshAnimationDeviceConfigurationChangeTest() throws Exception {
        final Instrumentation.ActivityMonitor activityMonitor =
                new Instrumentation.ActivityMonitor(WeatherForecastActivity.class.getName(),
                                                    null,
                                                    false);
        InstrumentationRegistry.getInstrumentation().addMonitor(activityMonitor);

        FlakyTest flakyTest = new FlakyTest(2, new FlakyTest.Test() {
            @Override
            public void execute() {
                MockResponse response = new MockResponse();
                response.setBody(TestData.getJsonResponse());
                response.setBodyDelay(10, TimeUnit.SECONDS);
                server.enqueue(response);

                Handler handler = new Handler(mActivityRule.getActivity().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActivityRule.getActivity().onRefresh();
                        SwipeRefreshLayout swipeRefreshLayout =
                                (SwipeRefreshLayout) mActivityRule.getActivity().findViewById(R.id.swipe_refresh_layout);
                        assertNotNull(swipeRefreshLayout);
                        assertTrue(swipeRefreshLayout.isRefreshing());
                        Utils.changeOrientation(mActivityRule.getActivity());
                    }
                });

                SystemClock.sleep(2000);

                //Get the new activity so we can check if the new SwipeRefreshLayout is set to refreshing
                Activity activity = activityMonitor.waitForActivityWithTimeout(5000);
                assertNotNull(activity);
                SwipeRefreshLayout swipeRefreshLayout =
                        (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout);
                assertNotNull(swipeRefreshLayout);
                assertTrue(swipeRefreshLayout.isRefreshing());
            }
        });

        flakyTest.run();
    }

    private void resetDatabase() {
        WeatherForecastDatabase weatherForecastDatabase =
                new WeatherForecastDatabase(mActivityRule.getActivity());
        SQLiteDatabase database = weatherForecastDatabase.getWritableDatabase();
        weatherForecastDatabase.onClear(database);
    }
}