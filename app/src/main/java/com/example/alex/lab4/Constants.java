package com.example.alex.lab4;

import android.provider.BaseColumns;

public final class Constants {

    public static final int VERSION = 1;
    public static final String DB_NAME = "student_grades.db";

    private Constants() {
        throw new AssertionError("Can't create constants class");
    }

    public static abstract class Course implements BaseColumns {
        public static final String TABLE_NAME = "students_grade";
        public static final String COURSE_ID = "COURSE_ID";
        public static final String COURSE_NAME = "COURSE_NAME";
        public static final String COURSE_GRADE = "COURSE_GRADE";
    }

}

