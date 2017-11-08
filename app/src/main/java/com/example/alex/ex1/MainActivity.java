package com.example.alex.ex1;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextWatcher, Chronometer.OnChronometerTickListener{

    public static final long START_VALUE_SEC = 60;  // 60 seconds.
    public static final long YELLOW_ALERT_VAL = 30; // 30 seconds.
    public static final long RED_ALERT_VAL = 10; // 10 seconds.
    
    private Chronometer chronometer;
    private TextView txtExercise, txtTotalRightAnswers;
    private EditText editTxt;
    private String exerciseString;
    private Random rand;
    private int rightAnswer;
    private int totalRightAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        txtExercise = findViewById(R.id.exerciseTxt);
        txtTotalRightAnswers = findViewById(R.id.totalRightAnswersTxt);
        editTxt = findViewById(R.id.answerEditText);

        editTxt.addTextChangedListener(this);
        chronometer.setOnChronometerTickListener(this);
        rand = new Random();

        rightAnswer = 0;
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
        chronometer.stop();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        Log.d("temp", SystemClock.elapsedRealtime() - chronometer.getBase() + "");
        if(SystemClock.elapsedRealtime() - chronometer.getBase() > 0) {
            gameOver();
        } else if(SystemClock.elapsedRealtime() - chronometer.getBase() > -RED_ALERT_VAL * 1000) {
            Log.d("temp", "BACKGROUND RED");
        } else if(SystemClock.elapsedRealtime() - chronometer.getBase() > -YELLOW_ALERT_VAL * 1000){
            Log.d("temp", "BACKGROUND YELLOW");

        }
    }

    private void changeScreenColor(int color) {

    }

    private void gameOver() {

    }
}
