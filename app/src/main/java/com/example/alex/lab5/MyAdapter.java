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

        int indexName = cursor.getColumnIndex(Constants.Course.COURSE_NAME);
        int indexGrade = cursor.getColumnIndex(Constants.Course.COURSE_GRADE);

        int gradeNumber = Integer.parseInt(cursor.getString(indexGrade));

        if(gradeNumber > 90) {
            view.setBackgroundColor(Color.GREEN);
        } else if (gradeNumber < 55) {
            view.setBackgroundColor(Color.RED);
        } else{
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        TextView name = view.findViewById(R.id.course_name);
        TextView grade = view.findViewById(R.id.course_grade);

        name.setText("Course Name: " + cursor.getString(indexName));
        grade.setText(" Course Grade: " + cursor.getString(indexGrade));

    }
}

