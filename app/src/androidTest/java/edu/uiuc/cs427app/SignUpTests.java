package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * tests for the sign up feature
 */
@RunWith(AndroidJUnit4.class)
public class SignUpTests {

    // launch LoginActivity for all test
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    // Check if input username and password are added to the database when sign-up is successful
    // (username and password fields are not empty, and no same username in database)
    @Test
    public void checkSuccessfulSignUp() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("yg28"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        String us = "yg28";
        String pa = "123";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/LoginDB.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor1 = db.rawQuery("Select * from users where username = ?", new String[] {us});
        Cursor cursor2 = db.rawQuery("Select * from users where password = ?", new String[] {pa});
        // check if the username only appears once in the database
        assertEquals(1, cursor1.getCount());
        // since it can have multiple same passwords, we check if this password in is in the database
        assertTrue(cursor2.getCount() > 0);

    }

    // test result when input username is empty
    @Test
    public void checkEmptyUsername() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("setupdatabase"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("setupdatabase"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        String pa = "1234";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/LoginDB.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("Select * from users where password = ?", new String[] {pa});
        //Since sign-up is not successful, there should only be no password "1234" stored
        assertTrue(cursor.getCount() == 0);
    }

    // test result when input password is empty
    @Test
    public void checkEmptyPassword() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("setupdatabase2"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("setupdatabase2"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("yg"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        String us = "yg";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/LoginDB.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("Select * from users where username = ?", new String[] {us});
        //Since sign-up is not successful, there should be no username "yg" stored
        assertTrue(cursor.getCount() == 0);
    }

    // test result when input username is already taken
    @Test
    public void checkTakenUsername() throws InterruptedException {
        onView(withId(R.id.username)).perform(typeText("setupdatabase3"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("setupdatabase3"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        // In case this test runs first, we need to add the duplicate pair here
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("yg28"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.username)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.username)).perform(typeText("yg28"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.signup)).perform(click());
        Thread.sleep(1000);
        String us = "yg28";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/edu.uiuc.cs427app/databases/LoginDB.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("Select * from users where username = ?", new String[] {us});
        //Since sign-up is not successful, there should be only one username "yg28" stored
        assertTrue(cursor.getCount() == 1);
    }

}
