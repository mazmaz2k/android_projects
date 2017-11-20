package com.example.alex.lab5;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class MyAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public MyAdapter(Context context, Cursor c) {
        super(context, c, true);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.single_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.course_name);
        TextView grade = view.findViewById(R.id.course_grade);

        name.setText("Course Name: " + cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_NAME)));
        grade.setText(" Course Grade: " + cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_GRADE)));

        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_GRADE))) > 90) {
            view.setBackgroundColor(Color.GREEN);
        } else if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_GRADE))) < 55) {
            view.setBackgroundColor(Color.RED);
        }
    }
}

