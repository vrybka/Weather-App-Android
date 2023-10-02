package edu.uiuc.cs427app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class UserCityDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "UserCity.db";
    public static final Integer DB_VERSION = 1;
    private static final String TAG = UserCityDB.class.getSimpleName();
    ArrayList<String> listOfCity = new ArrayList<String>();

    public UserCityDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database) {
        database.execSQL("create Table UserCity(id INT primary key, username TEXT , city TEXT)");
        ContentValues contentValues = new ContentValues();
    }

    @Override
    public void onUpgrade (SQLiteDatabase database, int i, int j) {

    }

    /**
     * method to insert the city to the specific user in db
     */
    public Boolean insertUserCity(String username, String cityName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        listOfCity.add(cityName);
        contentValues.put("username", username);
        contentValues.put("city", cityName);
        long result = db.insert("userCity", null, contentValues);

        if(result==-1) return false;
        else
            return true;
    }
    /**
     *  method to check if city of the specific user exist in db
     */
    public Boolean isExist(String username, String cityName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserCity where username = ?", new String[] {username});
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                int index = cursor.getColumnIndex("city");
                if (index >= 0) {
                    Log.d(TAG, "username: " + cursor.getString(index));
                    if (cursor.getString(index).equals(cityName)) {
                        return true;
                    }
                }
            } while (cursor.moveToNext());

        }
        return false;

    }
    /**
     * method  to remove the city to the specific user in db
     */

    public boolean removeUserCity(String username, String cityName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        listOfCity.remove(cityName);
        contentValues.put("username", username);
        contentValues.put("city", String.valueOf(listOfCity));
        long result = db.delete ("userCity",  "city = ? and username = ?", new String[] {cityName, username} );

        return result != -1;
    }

    /**
     * method to get the whole city list of the specific user from db
     */
    public ArrayList<String> getCityList(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserCity where username = ?", new String[] {username});
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                int index = cursor.getColumnIndex("city");
                if (index >= 0) {
                    Log.d(TAG, "username: " + cursor.getString(index));
                   // listOfCity.add(cursor.getString(index));
                    arr.add(cursor.getString(index));
                }
            } while (cursor.moveToNext());
        }
        return arr;
    }
}
