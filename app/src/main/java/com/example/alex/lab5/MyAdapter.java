package com.example.alex.lab5;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        name.setText(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_NAME)));
        grade.setText(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_GRADE)));
    }
}
//    private Cursor cursor;
//    private Context context;
//
//    MyAdapter(Context context, Cursor cursor) {
//        this.cursor = cursor;
//        this.context = context;
//        Log.d("tag1", "HELLO");
//        Log.d("tag1", cursor.getCount() + "Fucker");
//        Log.d("tag1", cursor.getColumnCount() + "");
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("tag1", "HELLO");
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View contactView = inflater.inflate(R.layout.single_row, parent, false);
//        return new MyViewHolder(contactView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        String name = "Course Name";
//        String grade = "100";
//        Log.d("tag1", "HELLO");
//
//        holder.course_grade.setText(grade);
//        holder.course_name.setText(name);
//    }
//
//    @Override
//    public int getItemCount() {
//        return cursor.getCount();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView course_name;
//        TextView course_grade;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            course_name = itemView.findViewById(R.id.course_name);
//            course_grade = itemView.findViewById(R.id.course_grade);
//        }
//    }
//}
