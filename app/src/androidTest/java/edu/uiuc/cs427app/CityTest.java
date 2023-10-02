package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

/**
 * City Unit Test
 */
public class CityTest {
    private static final String TEST_CITY = "Espresso Location Test City 64";
    private static final String DB_PATH = "/data/data/edu.uiuc.cs427app/databases/UserCity.db";

    private static SQLiteDatabase db;
    private static String username;

    static Intent initIntent;
    static {
        initIntent = new Intent(ApplicationProvider.getApplicationContext(), MainActivity.class);
        initIntent.putExtra("theme", false);
        initIntent.putExtra("userName", "Team#37-td2");
    }

    @Rule
    public ActivityScenarioRule<MainActivity> ActivityScenarioRule =
            new ActivityScenarioRule<>(initIntent);


    @BeforeClass
    public static void setup() {
        db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        username = initIntent.getStringExtra("userName");
    }

    @AfterClass
    public static void tearDown() {
        db.close();
    }

    @Test
    public void checkAddLocation() throws InterruptedException {
        // check if db exist this city
        String testCity = "San Jose";

        Cursor beforeCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {testCity, username});
        assertEquals(0, beforeCursor.getCount());


        // check to add a new city
        onView(withId(R.id.buttonAddLocation)).perform(click());
        onView(withText("ADD CITY")).inRoot(isDialog()).check(matches(isDisplayed()));
        Thread.sleep(1000);
        onView(withId(R.id.editText)).perform(typeText(testCity), closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withText("ADD CITY")).perform(click());

        Cursor afterCursor = db.rawQuery("Select * from UserCity where city = ? and username = ? ", new String[] {testCity, username});
        assertEquals(1, afterCursor.getCount());

        db.delete ("userCity",  "city = ? and username = ?", new String[] {testCity, username} );
        Cursor removeCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {testCity, username});
        assertEquals(0, removeCursor.getCount());
    }

    @Test
    public void RemoveExitingLocationTest() {

        // insert testing location
        onView(withId(R.id.buttonAddLocation))
                .perform(click());

        onView(withText("ADD CITY"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(R.id.editText))
                .perform(typeText(TEST_CITY), closeSoftKeyboard());

        onView(withText("ADD CITY")).perform(click());

        Cursor beforeCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {TEST_CITY, username});
        assertEquals(1, beforeCursor.getCount());

        // remove added city
        onView(withId(R.id.buttonRemoveLocation))
                .perform(click());

        onView(withText("Remove City"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(R.id.editText))
                .perform(typeText(TEST_CITY), closeSoftKeyboard());

        onView(withText("Remove City"))
                .perform(click());

        Cursor afterCursor = db.rawQuery("Select * from UserCity where city = ? and username = ?", new String[] {TEST_CITY, username});
        assertEquals(0, afterCursor.getCount());
    }
}

