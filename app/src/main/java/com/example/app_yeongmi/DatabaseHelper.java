package com.example.app_yeongmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


// create Database for login and sign up
public class DatabaseHelper extends SQLiteOpenHelper {



    public DatabaseHelper(@Nullable Context context) {
        super(context, "Signup.db", null, 1);
    }

    // create Database
    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table alluseres(email TEXT primary key, password TEXT)");
    }

    // check if there is a database
    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("drop Table if exists alluseres");
    }


    // insert in database
    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("alluseres", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    // check if email and passsword pair are in database
    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from alluseres where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
