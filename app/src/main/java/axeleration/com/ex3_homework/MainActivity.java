package axeleration.com.ex3_homework;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public final int REQUEST_CALL_LOG_CODE = 1234;
    private TextView nameTxt;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendSms = findViewById(R.id.sendSMSBtn);
        Button getRandomText = findViewById(R.id.getRandomTextBtn);

        nameTxt = findViewById(R.id.nameBBF);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) { // Runtime ask for permissions.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.SEND_SMS}, REQUEST_CALL_LOG_CODE);
        } else {
            getAllCallLogs();   // All permissions granted.
        }

        sendSms.setOnClickListener(this);
        getRandomText.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CALL_LOG_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    getAllCallLogs();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied!\nThe application may not work properly.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getAllCallLogs() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor mCursor = getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER},
                null, null, CallLog.Calls.NUMBER + " DESC");

        if(mCursor != null) {
            getBBFData(mCursor);
        }
    }

    private void getBBFData(Cursor cursor) {
        HashMap<String, Integer> mapCalls = new HashMap<>();
        HashMap<String, String> mapNames = new HashMap<>();
        cursor.moveToFirst();

        Integer max = 0;
        String maxKey = "";

        do {
            String key = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if(mapCalls.get(key) == null) {
                mapCalls.put(key, 0);
                mapNames.put(key, name);
            }
            mapCalls.put(key, mapCalls.get(key) + 1);
        }while(cursor.moveToNext());

        for(String key: mapCalls.keySet()) {
            if(mapCalls.get(key) > max) {
                max = mapCalls.get(key);
                maxKey = key;
            }
        }

        nameTxt.setText(mapNames.get(maxKey));
        number = maxKey;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendSMSBtn:
                sendSms();
                break;
            case R.id.getRandomTextBtn:
                getRandomText();

                break;
        }
    }

    private void getRandomText() {
        new MyTask().execute("https://talaikis.com/api/quotes/random/");
    }

    private void sendSms() {
        TextView randomTxt = findViewById(R.id.randomText);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number,null, randomTxt.getText().toString(), null, null);
        Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
    }


    private class MyTask extends AsyncTask<String, Void, Void> {

        private ProgressBar bar;
        private TextView randomTxt;
        private String quote = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = findViewById(R.id.process_bar);
            bar.setVisibility(View.VISIBLE);
            randomTxt = findViewById(R.id.randomText);
            randomTxt.setText("");
        }

        @Override
        protected Void doInBackground(String... strings) {

            String json = getJSON(strings[0]);
            try {
                JSONObject object = new JSONObject(json);
                Thread.sleep(500);
                quote = object.get("quote").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Button sendSms = findViewById(R.id.sendSMSBtn);
            bar.setVisibility(View.INVISIBLE);
            randomTxt.setText(quote);
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSms.setEnabled(true);
            }
        }

        private String getJSON(String url) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.connect();
                int status = c.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        //disconnect error
                    }
                }
            }
            return null;
        }
    }

}

