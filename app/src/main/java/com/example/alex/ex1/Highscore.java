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

        // Getting date and score from intent.
        String date = intent.getStringExtra("date");
        int score = intent.getIntExtra("score", 0);

        // Gets the data repository in write mode.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys.
        ContentValues values = new ContentValues();
        values.put(Constants.Score.COL_DATE, date);
        values.put(Constants.Score.COL_SCORE, score);

        // Insert into the table.
        db.insert(Constants.Score.TABLE_NAME, null, values);

        // Read exists data from database and append it to the text view.
        readFromDB(db);

        db.close();
    }

    private void readFromDB(SQLiteDatabase db) {
        // Select * From Table
        Cursor c = db.query(Constants.Score.TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        // Making a string.
        StringBuilder info = new StringBuilder("");

        // Getting the indexes of the columns.
        int index_date = c.getColumnIndex(Constants.Score.COL_DATE);
        int index_score = c.getColumnIndex(Constants.Score.COL_SCORE);

        // Append the data to String and then to the text view.
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
