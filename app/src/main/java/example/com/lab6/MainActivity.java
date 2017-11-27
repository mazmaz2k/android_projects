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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleApiClient mGoogleApiClient;
    private final int MY_CODE = 0;
    private EditText longitude, latitude;
    private double my_longitude, my_latitude, frind_longitude, friend_latitude;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        submit = findViewById(R.id.Submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude.getText().toString().equals("") || latitude.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                frind_longitude = Double.parseDouble(longitude.getText().toString());
                friend_latitude = Double.parseDouble(latitude.getText().toString());

                Log.d("temp", friend_latitude + " " + frind_longitude + "\n" + my_latitude + " " + my_longitude);
            }
        });

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_CODE);


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
        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    finish();
                }
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d("temp", lastLocation.toString());
//            Toast.makeText(this, lastLocation.getLatitude() + " " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//            my_latitude = lastLocation.getLatitude();
//            my_longitude = lastLocation.getLongitude();

        } else {
            Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("temp", "Error " + connectionResult.toString());
    }


    @Override
    public void onLocationChanged(Location location) {
//        my_latitude = location.getLatitude();
//        my_longitude = location.getLongitude();
//
//        Location startPoint = new Location("locationA");
//        startPoint.setLatitude(my_latitude);
//        startPoint.setLongitude(my_longitude);
//
//        Location endPoint = new Location("locationB");
//        endPoint.setLatitude(friend_latitude);
//        endPoint.setLongitude(frind_longitude);
//
//        double distance = startPoint.distanceTo(endPoint);
//        Log.d("temp", "\n\n\nThe Distance is " + distance);
        Toast.makeText(this, "CHanged Location", Toast.LENGTH_SHORT).show();
    }


}
