package com.example.alex.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText text;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.submitBtn);
        text = (EditText) findViewById(R.id.editText);
        intent = getIntent();
        String s = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(s != null) {
            text.setText(s);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, secondActivity.class);
                i.putExtra("key", text.getText().toString());
                startActivity(i);
            }
        });

    }
}
