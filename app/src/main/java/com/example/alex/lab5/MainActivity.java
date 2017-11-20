package com.example.alex.lab5;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText course_id, course_name, course_grade;
    private SQLiteDatabase db;
    private TextView best_course, average;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        course_id = findViewById(R.id.course_code_edit_text);
        course_name = findViewById(R.id.course_name_edit_text);
        course_grade = findViewById(R.id.course_grade_edit_text);
        Button submit_btn = findViewById(R.id.submit_btn);
        best_course = findViewById(R.id.best_course);
        average = findViewById(R.id.average);
        best_course = findViewById(R.id.best_course);
        listView = findViewById(R.id.listView);

        submit_btn.setOnClickListener(this);

        getData();
    }

    private void insertIntoTable(int id, String name, int grade) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.Course.COURSE_ID, id);
        cv.put(Constants.Course.COURSE_NAME, name);
        cv.put(Constants.Course.COURSE_GRADE, grade);

        this.db.insert(Constants.Course.TABLE_NAME,null, cv);
    }

    private void getData() {
        Cursor c = this.db.query(Constants.Course.TABLE_NAME, new String[] {Constants.Course.COURSE_NAME +" ,MAX(" + Constants.Course.COURSE_GRADE +")"+ ", AVG(" + Constants.Course.COURSE_GRADE + ")"}, null, null,null,null, null);   // Select * From table
        Cursor cursor = this.db.query(Constants.Course.TABLE_NAME,null,null,null,null,null, Constants.Course.COURSE_NAME +" ASC");
        MyAdapter adapter = new MyAdapter(MainActivity.this, cursor);
        listView.setAdapter(adapter);
        c.moveToFirst();
        best_course.setText("Course name: " + c.getString(0) + "\nHighest Grade:" + c.getInt(1));
        average.setText("Average: " + c.getString(2));
        c.close();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit_btn) {
            if(course_id.getText().toString().equals("") || course_name.getText().toString().equals("") || course_grade.getText().toString().equals("")) {
                Toast.makeText(this, "Please make sure that all the fields are filled", Toast.LENGTH_SHORT).show();
                return;
            }
            int id = Integer.parseInt(course_id.getText().toString());
            String name = course_name.getText().toString();
            int grade = Integer.parseInt(course_grade.getText().toString());

            insertIntoTable(id, name, grade);
            getData();

            course_grade.setText("");
            course_id.setText("");
            course_name.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
