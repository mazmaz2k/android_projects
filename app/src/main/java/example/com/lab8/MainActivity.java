package example.com.lab8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_CODE = 12345;
    private Intent intent;
    private Button btnStart, btnStop;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkMyPermissions();
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);

        intent = new Intent(this, MyService.class);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumber.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill the phone number!", Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("number", phoneNumber.getText().toString());
                startService(intent);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                phoneNumber.setEnabled(false);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                phoneNumber.setEnabled(true);
                phoneNumber.setText("");
            }
        });
    }

    private void checkMyPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_CODE);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Cannot send SMS without permissions!", Toast.LENGTH_SHORT).show();
                btnStart.setEnabled(false);
            } else
                btnStart.setEnabled(true);
        }
    }
}
