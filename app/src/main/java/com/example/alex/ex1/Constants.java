package com.example.alex.ex1;

import android.provider.BaseColumns;

public final class Constants {

    public static final int VERSION = 1;
    public static final String DB_NAME = "high_score.db";

    private Constants() {
        throw new AssertionError("Can't create constants class");
    }

    public static abstract class Score implements BaseColumns {
        public static final String TABLE_NAME = "HIGH_SCORES";
        public static final String COL_DATE = "DATE";
        public static final String COL_SCORE = "SCORE";
    }

}

