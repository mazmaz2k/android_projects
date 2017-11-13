package com.example.alex.ex1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.Score.TABLE_NAME + " (" + Constants.Score._ID + " NUMBER PRIMARY KEY, " + Constants.Score.COL_DATE  + " TEXT, " + Constants.Score.COL_SCORE + " NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Score.TABLE_NAME);
        onCreate(db);
    }
}
