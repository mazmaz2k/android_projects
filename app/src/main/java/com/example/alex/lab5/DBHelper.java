package com.example.alex.lab5;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.Course.TABLE_NAME + " (" + Constants.Course._ID + " NUMBER PRIMARY KEY, " + Constants.Course.COURSE_ID +" NUMBER, " + Constants.Course.COURSE_NAME + " TEXT, " + Constants.Course.COURSE_GRADE + " NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Course.TABLE_NAME);
        onCreate(db);
    }
}
