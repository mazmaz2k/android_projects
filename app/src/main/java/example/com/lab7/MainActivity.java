package example.com.lab7;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView txtJoke;
    private final String MY_URL = "http://api.icndb.com/jokes/random";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetJoke = findViewById(R.id.btnPress);
        txtJoke = findViewById(R.id.txtJoke);

        btnGetJoke.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetJokeTask().execute(MY_URL);
            }
        });
    }

    private class GetJokeTask extends AsyncTask<String, Void, Void> {

        private ProgressBar bar;
        private String joke = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar = findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);
            txtJoke.setText("");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String json = getJSON(strings[0]);
                JSONObject obj = new JSONObject(json);
                joke = ((JSONObject)obj.get("value")).get("joke").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bar.setVisibility(View.INVISIBLE);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            txtJoke.setText(joke);
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
