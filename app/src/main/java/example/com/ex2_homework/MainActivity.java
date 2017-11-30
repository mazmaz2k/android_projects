package example.com.ex2_homework;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public final int PERMISSION_CODE_REQUEST = 15978;
    private SQLiteDatabase database;
    private ListView list;
    private MyAdapter adapter;
    private TelephonyManager telephonyManager;
    private CellInfo cellInfo;
    private GoogleApiClient googleApiClient;
    private Location myLocation;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_LONG).show();
            return;
        }

        /* Request for permissions if the permissions is not granted. */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_CODE_REQUEST);
        }
        else {  // There are all needed permissions.
            cellInfo = telephonyManager.getAllCellInfo().get(0);   //This will give info of all sims present inside your mobile
        }
        list = findViewById(R.id.list_view);
        DBHelper dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();


//        int asuLevel = getAsuLevel(cellInfo);
//
//        insertData("123","321", getDate(), asuLevel);
//
//
//        Cursor c = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null,null,null);
//
//        Log.d("temp", c.getCount() + "");
//        adapter = new MyAdapter(this, c);
//
//        list.setAdapter(adapter);
        if (googleApiClient == null) {
            buildGoogleApi();
        }
        createLocationRequest();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    /* Returns the asu level of the mobile phone. */
    private int getAsuLevel(CellInfo cellInfo) {
        int asuLevel = 0;
        if (cellInfo instanceof CellInfoGsm) {
            asuLevel = ((CellInfoGsm) cellInfo).getCellSignalStrength().getAsuLevel();
        } else if (cellInfo instanceof CellInfoLte) {
            asuLevel = ((CellInfoLte) cellInfo).getCellSignalStrength().getAsuLevel();
        } else if (cellInfo instanceof CellInfoCdma) {
            asuLevel = ((CellInfoCdma) cellInfo).getCellSignalStrength().getAsuLevel();
        } else if (cellInfo instanceof CellInfoWcdma) {
            asuLevel = ((CellInfoWcdma) cellInfo).getCellSignalStrength().getAsuLevel();
        }
        return asuLevel;
    }

    /* Returns the current date and time.  */
    private String getDate() {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss", Locale.getDefault());
        return date.format(Calendar.getInstance().getTime());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE_REQUEST:
                if (permissions.length > 0 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted.", Toast.LENGTH_LONG).show();
                    cellInfo = telephonyManager.getAllCellInfo().get(0);   //This will give info of all sims present inside your mobile
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied.\nThe application may not work properly!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /* Insert data to the database. */
    private void insertData(String latitude, String longitude, String date, int asuLevel) {
        ContentValues values = new ContentValues();
        values.put(Constants.ASU.LATITUDE, latitude);
        values.put(Constants.ASU.LONGITUDE, longitude);
        values.put(Constants.ASU.DATE, date);
        values.put(Constants.ASU.ASU, asuLevel);
        long returnValue = database.insert(Constants.ASU.TABLE_NAME, null, values);
        if (returnValue != -1) {
            Toast.makeText(this, "Database updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("temp", location.getLatitude() + "-" + location.getLongitude());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
