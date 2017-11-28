package example.com.lab6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private final int MY_CODE_REQUEST = 12345;
    private Location myLocation, friendLocation;
    private EditText editTextLongitude, editTextLatitude, editTextPhone;
    private Button btnSubmit;
    private TextView txtShow;
    private LocationRequest mLocationRequest;
    private boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLongitude = findViewById(R.id.longitude);
        editTextLatitude = findViewById(R.id.latitude);
        editTextPhone = findViewById(R.id.phone_number);
        btnSubmit = findViewById(R.id.Submit);
        txtShow = findViewById(R.id.txt_send);

        txtShow.setText("");
        firstRun = true;

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLongitude.getText().toString().equals("") || editTextLatitude.getText().toString().equals("") || editTextPhone.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double longitude = Double.parseDouble(editTextLongitude.getText().toString());
                double latitude = Double.parseDouble(editTextLatitude.getText().toString());

                Location friend = new Location("LocationFriend");
                friend.setLatitude(latitude);
                friend.setLongitude(longitude);
                friendLocation = friend;

                editTextLatitude.setEnabled(false);
                editTextLongitude.setEnabled(false);
                editTextPhone.setEnabled(false);
                btnSubmit.setEnabled(false);

                startLocationUpdate();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, MY_CODE_REQUEST);
        }
        createLocationRequest();
        buildGoogleApi();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApi() {
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CODE_REQUEST:
                if(grantResults.length < 2 ||
                        grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    btnSubmit.setEnabled(false);
                    Toast.makeText(this, "You must to allow all the permissions to fully use of the application.", Toast.LENGTH_LONG).show();
                }
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayLocation();
                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            displayLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    @Override
    public void onLocationChanged(Location location) {
        double distanceMoved = 0;
        if(myLocation != null)
        {
            distanceMoved = location.distanceTo(myLocation);
        }
        if(distanceMoved >= 10 || firstRun) {
            if(firstRun)
                firstRun = false;
            sendSMS();
        } else {
            Toast.makeText(this, "Distance is not changed over 10 meters", Toast.LENGTH_SHORT).show();
        }

        myLocation = location;
    }

    private void sendSMS() {
        final double distance = myLocation.distanceTo(friendLocation);
        final String message = "I'm " + distance + " meters from you!";
        final String phoneNumber = editTextPhone.getText().toString();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+972" + phoneNumber, null, message, null, null);
        Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();
        txtShow.setText(String.valueOf(distance));
    }

    private void displayLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(lastLocation != null) {
                myLocation = lastLocation;
            }
        }
    }
}
