package example.com.ex2_homework;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.view.View;
import android.widget.Button;
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
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    public final int SMALLEST_DISPLACEMENT = 10;    // Meters
    public final int QUERY_INTERVAL = 60000;  // Milliseconds
    public final int QUERY_FAST_INTERVAL = 60000;    // Milliseconds
    public final int PERMISSION_CODE_REQUEST = 15978;

    private SQLiteDatabase database;
    private MyAdapter adapter;
    private TelephonyManager telephonyManager;
    private CellInfo cellInfo;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Cursor cursor;
    private Button btn_sort_by_date, btn_sort_by_asu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sort_by_asu = findViewById(R.id.btn_sort_by_asu);
        btn_sort_by_date = findViewById(R.id.btn_sort_by_date);

        btn_sort_by_date.setOnClickListener(this);
        btn_sort_by_asu.setOnClickListener(this);

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);      // The the system data about the phone.
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
        } else {  // There are all needed permissions.
            if (telephonyManager.getAllCellInfo().size() == 0) {        // Cannot read the cell data.
                Toast.makeText(this, "Cell data not found!", Toast.LENGTH_LONG).show();
            } else {
                cellInfo = telephonyManager.getAllCellInfo().get(0);   //This will give info of all sims present inside your mobile
            }
        }
        ListView list = findViewById(R.id.listView);
        DBHelper dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        cursor = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null, null, null);
        /* Enable buttons if the database is not empty. */
        if (cursor.getCount() != 0) {
            btn_sort_by_date.setEnabled(true);
            btn_sort_by_asu.setEnabled(true);
        }
        adapter = new MyAdapter(this, cursor);

        list.setAdapter(adapter);

        /* Connect to google API services. */
        if (googleApiClient == null) {
            buildGoogleApi();
        }
        createLocationRequest();
    }

    /* Location connection settings. */
    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(QUERY_INTERVAL);                        // Update the location every 60 seconds.
        locationRequest.setFastestInterval(QUERY_FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);     // Update the location if we moved over 10 meters.
    }

    /* Connect to google API. */
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
        if(cellInfo == null)
            return asuLevel;
        else if (cellInfo instanceof CellInfoGsm) {
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
                    if(telephonyManager.getAllCellInfo().size() == 0) {
                        Toast.makeText(this, "No SIM found!", Toast.LENGTH_LONG).show();
                    }else {
                        cellInfo = telephonyManager.getAllCellInfo().get(0);   //This will give info of all sims present inside your mobile
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
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
        if (returnValue == -1) {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection error!", Toast.LENGTH_SHORT).show();
    }

    /* Insert the location into the database when changed. */
    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        insertData(String.valueOf(latitude), String.valueOf(longitude), getDate(), getAsuLevel(cellInfo));
        cursor.close(); // free the cursor point memory.
        cursor = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null, null);
        adapter.changeCursor(cursor);   // update the adapter with new cursor.
        if(cursor.getCount() != 0) {    // if the database is not empty enable the buttons.
            btn_sort_by_asu.setEnabled(true);
            btn_sort_by_date.setEnabled(true);
        }

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
        cursor.close();
        database.close();
    }
    /* Remove one row with id _id from the database and update the list view. */
    public void removeRow(int _id) {
        database.delete(Constants.ASU.TABLE_NAME, Constants.ASU._ID + " = ?", new String[] {String.valueOf(_id)});
        cursor.close();
        cursor = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null, null);
        adapter.changeCursor(cursor);
        if(cursor.getCount() == 0) {    // if the database is empty disable the buttons.
            btn_sort_by_date.setEnabled(false);
            btn_sort_by_asu.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sort_by_date:
                sortByDate();
                break;
            case R.id.btn_sort_by_asu:
                sortByAsu();
                break;
        }
    }

    /* Sort the list view by date */
    private void sortByDate() {
        cursor.close();
        cursor = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null, Constants.ASU.DATE + " DESC");
        adapter.swapCursor(cursor);
    }

    /* Sort the list view by ASU level. */
    private void sortByAsu() {
        cursor.close();
        cursor = database.query(Constants.ASU.TABLE_NAME, null, null, null, null, null, Constants.ASU.ASU + " DESC");
        adapter.swapCursor(cursor);
    }
}
