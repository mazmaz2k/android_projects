package com.example.alex.ex1;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements TextWatcher, Chronometer.OnChronometerTickListener, View.OnClickListener{

    public static final long START_VALUE_SEC = 60;  // 60 seconds.
    public static final long YELLOW_ALERT_VAL = 30; // 30 seconds.
    public static final long RED_ALERT_VAL = 10; // 10 seconds.

    private ViewGroup layout;
    private Chronometer chronometer;
    private TextView txtExercise, txtTotalRightAnswers;
    private EditText editTxt;
    private String exerciseString;
    private Random rand;
    private int rightAnswer;
    private int totalRightAnswers;
    private long chronometer_saved_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.mainLayout);
        chronometer = findViewById(R.id.chronometer);
        txtExercise = findViewById(R.id.exerciseTxt);
        txtTotalRightAnswers = findViewById(R.id.totalRightAnswersTxt);
        editTxt = findViewById(R.id.answerEditText);

        editTxt.addTextChangedListener(this);
        chronometer.setOnChronometerTickListener(this);
        txtTotalRightAnswers.setOnClickListener(this);
        rand = new Random();

        rightAnswer = 0;
        chronometer_saved_time = 0;
        totalRightAnswers = 0;
        exerciseString = "";

        initChronometer();
        setExercise();

    }

    private void initChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime() + START_VALUE_SEC * 1000);
        chronometer.start();
    }

    private void setExercise() {
        int r = rand.nextInt(15) + 1;
        exerciseString =  rightAnswer + " + " + r + " = ";
        rightAnswer = rightAnswer + r;
        txtExercise.setText(exerciseString);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().equals("")) {return ;}
        boolean isPassed = checkAnswer(Integer.parseInt(s.toString()));
        if(isPassed) {
            updateView(++totalRightAnswers);
            editTxt.setText("");
            setExercise();
        }
    }

    private boolean checkAnswer(int answer) {
        return answer == rightAnswer;
    }

    private void updateView(int number) {
        txtTotalRightAnswers.setText(String.valueOf(number));
    }

    @Override
    protected void onPause() {
        super.onPause();
        chronometer_saved_time = SystemClock.elapsedRealtime() - chronometer.getBase();        // Update the time left when the activity goes onPause().
        chronometer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(chronometer_saved_time != 0) {    // If not the first application run.
            chronometer.setBase(SystemClock.elapsedRealtime() - chronometer_saved_time);     // Set last time before leave the activity.
        }
        chronometer.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(chronometer_saved_time > 0) {
            restart();
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if(SystemClock.elapsedRealtime() - chronometer.getBase() > 0) {
            gameOver();
        } else if(SystemClock.elapsedRealtime() - chronometer.getBase() > -RED_ALERT_VAL * 1000) {
            changeScreenColor(Color.RED);
        } else if(SystemClock.elapsedRealtime() - chronometer.getBase() > -YELLOW_ALERT_VAL * 1000){
            changeScreenColor(Color.YELLOW);

        }
    }

    private void changeScreenColor(int color) {
        layout.setBackgroundColor(color);
    }

    private void gameOver() {
        chronometer.stop();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/YYYY 'at' hh:mm:ss", Locale.US);
        date.setTimeZone(TimeZone.getDefault());
        String strDate = date.format(new Date(System.currentTimeMillis()));
        Intent intent = new Intent(MainActivity.this, Highscore.class);
        intent.putExtra("date", strDate);
        intent.putExtra("score", totalRightAnswers);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.totalRightAnswersTxt) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:9jK-NcRmVcw"));
            if(intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, youtube application not found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void restart() {
        changeScreenColor(Color.TRANSPARENT);
        editTxt.setText("");
        totalRightAnswers = 0;
        chronometer_saved_time = 0;
        rightAnswer = 0;
        initChronometer();
        setExercise();
        updateView(totalRightAnswers);
    }
}
