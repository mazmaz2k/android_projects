package com.example.alex.lab3;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class secondActivity extends AppCompatActivity {

    TextView txt2;
    Button spkBtn;
    TextToSpeech txtSpeach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent i = getIntent();
        String str = i.getStringExtra("key");
        spkBtn = (Button) findViewById(R.id.speakBtn);
        txt2 = (TextView) findViewById(R.id.sentence);
        txt2.setText(str);

        txtSpeach = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    txtSpeach.setLanguage(Locale.US);
                }
            }
        });

        spkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSpeach.speak(txt2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}
