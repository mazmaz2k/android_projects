package com.example.alex.ex1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Highscore extends AppCompatActivity {

    private TextView high_scores_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        high_scores_txt = findViewById(R.id.high_scores);

        DBHelper dbHelper = new DBHelper(this);
        Intent intent = getIntent();

        String date = intent.getStringExtra("date");
        int score = intent.getIntExtra("score", 0);

        // Gets the data repository in write mode.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys.
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_DATE, date);
        values.put(DBHelper.COL_SCORE, score);

        db.insert(DBHelper.TABLE_NAME, null, values);

        readFromDB(db);

        db.close();
    }

    private void readFromDB(SQLiteDatabase db) {
        Cursor c = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        StringBuilder info = new StringBuilder("");
        int index_date = c.getColumnIndex(DBHelper.COL_DATE);
        int index_score = c.getColumnIndex(DBHelper.COL_SCORE);

        do {
            info.append(c.getString(index_date));
            info.append(" |------| ");
            info.append(c.getString(index_score));
            info.append("\n");
        } while(c.moveToNext());

        high_scores_txt.setText(info);
        c.close();
    }
}
