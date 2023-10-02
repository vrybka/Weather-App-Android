package edu.uiuc.cs427app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "LoginDB.db";

    public DBHelper(Context context) {
        super(context,"LoginDB.db", null, 1);
    }

    //create database to store username and password data
    @Override
    public void onCreate (SQLiteDatabase database) {
        database.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase database, int i, int j) {
        database.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String us, String pa) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", us);
        contentValues.put("password", pa);
        long result = database.insert("users", null, contentValues);

        return result != -1;
    }


    public Boolean checkUserNameTaken(String username){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from users where username = ?", new String[] {username});

        return cursor.getCount() > 0;
    }


    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});
        return cursor.getCount() > 0;
    }
}
