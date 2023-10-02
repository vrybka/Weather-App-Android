package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.StringContains.containsString;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

public class LocationMapTest {
    static Intent initIntent;
    static {
        initIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        initIntent.putExtra("theme", false);
        initIntent.putExtra("userName", "Team#37-testmap");
    }

    final static String CITY_1 = "San Jose";
    final static String CITY_2 = "Los Angeles";
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(initIntent);

    @Test
    public void  LocationMapTest() throws InterruptedException {
        // check if db exist this city
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/UserCity.db", null, SQLiteDatabase.OPEN_READWRITE);
        String username = initIntent.getStringExtra("userName");

        Cursor beforeCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {CITY_1, username});
        if(beforeCursor.getCount() == 0){
            onView(withId(R.id.buttonAddLocation)).perform(click());
            onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.editText)).perform(typeText(CITY_1), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withText("ADD CITY")).perform(click());
        }
        Cursor beforeCursor_2 = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {CITY_2, username});
        if(beforeCursor_2.getCount() == 0){
            onView(withId(R.id.buttonAddLocation)).perform(click());
            onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
            Thread.sleep(1000);
            onView(withId(R.id.editText)).perform(typeText(CITY_2), closeSoftKeyboard());
            Thread.sleep(1000);
            onView(withText("ADD CITY")).perform(click());
        }

        ViewInteraction mButton = onView(
                allOf(withId(R.id.mBtnMap), withText("Show Map"),
                        childAtPosition(
                                withParent(withId(R.id.list)),
                                2),
                        isDisplayed()));
        mButton.perform(click());

        //UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //UiObject marker = mDevice.findObject(new UiSelector().descriptionContains("Marker in Sydney"));

        // Test Location San Jose
        //onView(withId(R.id.mBtnMap)).perform(click());
        Thread.sleep(1500);
        //onView(withIndex(withId(R.id.mBtnMap), 1)).perform(click());
//        onView(withId(R.id.weatherinfo)).check(matches(withText(containsString(CITY_1))));
//        // Since weather page is Activity type, need to use pressBack twice
//        pressBack();
//        pressBack();
//
//        // Test Location Los Angeles
//        onView(withId(R.id.mBtn - 1)).perform(click());
//        Thread.sleep(1500);
//        onView(withId(R.id.weatherinfo)).check(matches(withText(containsString(CITY_2))));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}

