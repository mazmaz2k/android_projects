package com.example.alex.lab4;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DB_NAME = "student_grades.db";

    public final class Constants {
        private Constants() {
            throw new AssertionError("Can't create constants class");
        }
    }

    public static abstract class Course implements BaseColumns {
        public static final String TABLE_NAME = "students_grade";
        public static final String COURSE_ID = "COURSE_ID";
        public static final String COURSE_NAME = "COURSE_NAME";
        public static final String COURSE_GRADE = "COURSE_GRADE";
    }


    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Course.TABLE_NAME + " (" + Course.COURSE_ID +" NUMBER PRIMARY KEY, " + Course.COURSE_NAME + " TEXT, " + Course.COURSE_GRADE + " NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_NAME);
        onCreate(db);
    }
}
