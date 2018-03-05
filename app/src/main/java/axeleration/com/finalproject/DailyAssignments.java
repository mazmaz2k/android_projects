package axeleration.com.finalproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/* This class for showing all future daily tasks. */
public class DailyAssignments extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase db;
    private TaskCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_assignments);
        db = DBHelperSingleton.getInstanceDBHelper(this).getReadableDatabase(); // open db from singleton object.
    }

    /* Menu options initialize */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Option choose for a menu  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sort_by_date:     // sort by date
                cursor = getTaskCursor(Constants.TASKS.DATETIME);   // get cursor order by date time.
                break;
            case R.id.sort_by_name:     // sort by name
                cursor = getTaskCursor(Constants.TASKS.FULL_NAME);  // get cursor order by full name.
                break;
            case R.id.sort_by_location:   // sort by location
                updateAllDBLocation();      //  update the distance between us and the client in the DB.
                cursor = getTaskCursor(Constants.TASKS.LOCATION);   // get cursor order by location latitude and longitude.
                break;
            default:
                onBackPressed();        // back button pressed.
                break;
        }
        adapter = new TaskCursorAdapter(this,cursor);
        listView.setAdapter(adapter);
        return true;
    }

    /* Updates the distance between us and the client in the DB */
    private void updateAllDBLocation() {
        cursor = db.query(Constants.TASKS.TABLE_NAME,   // get the cursor with non done tasks IS_SIGN = 0.
                null,
                Constants.TASKS.IS_SIGN + "=?", // 0 For non done tasks or 1 for tasks done.
                new String[] {"0"},
                null,
                null,
                null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++) {    // for each row in DB.
            int ID = cursor.getInt(cursor.getColumnIndex(Constants.TASKS._ID)); // get the ID.
            String address = cursor.getString(cursor.getColumnIndex(Constants.TASKS.ADDRESS));  // get the ADDRESS.
            Location location = getLocation(address);   // gets the location by address string.
            ContentValues values = getContentValues(location);  // values that should be pushed into db.
            if(values == null) {    // nothing to push
                continue;
            }
            db.update(Constants.TASKS.TABLE_NAME, values, Constants.TASKS._ID + "=?", new String[] {String.valueOf(ID)});   // update the DB ADDRESS field.
            cursor.moveToNext();    // go to the next row.
        }
    }

    /* This function returns a new location object from address string */
    private Location getLocation(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Address addressObj = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1); // get address list from string
            addressObj = addresses.get(0);  // get first object address
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addressObj == null) {    // the address is invalid or not found in map.
            Toast.makeText(this, "The address is invalid!", Toast.LENGTH_SHORT).show();
            return null;
        }
        double longitude = addressObj.getLongitude();   // get longitude of the address.
        double latitude = addressObj.getLatitude(); // get latitude of the address.

        Location location = new Location("");   // new location object.
        location.setLongitude(longitude);   // set location longitude.
        location.setLatitude(latitude);     // set location latitude.

        return location;
    }

    /* This function will return a new contentValues object with the new location of the address from current location */
    private ContentValues getContentValues(Location location) {
        if(location == null)    // location not found.
            return null;
        ContentValues values = new ContentValues();
        Location myLocation = MainActivity.myCurrentLocation;   // get my current location from static function in main activity.
        if(myLocation == null) {    // my location is null on emulators.
            return null;
        }
        values.put(Constants.TASKS.LOCATION, myLocation.distanceTo(location));  // put location values in content values.
        return values;
    }

    /* Update the view list onResume the activity */
    @Override
    protected void onResume() {
        super.onResume();
        listView = findViewById(R.id.listViewTaskes);
        cursor = getTaskCursor(Constants.TASKS.DATETIME);
        adapter = new TaskCursorAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    /* Return cursor after open the DB */
    private Cursor getTaskCursor(String orderBy) {
        cursor = db.query(Constants.TASKS.TABLE_NAME,
                null,
                Constants.TASKS.IS_SIGN + "=? AND " + Constants.TASKS.DATE + "=? AND "+ Constants.TASKS.DATETIME + ">=?",   // query to get today date and future time
                new String[] {"0", StaticFunctions.getCurrentDate("d-M-yyyy"), StaticFunctions.getCurrentDate("yyyy-MM-dd HH:mm:ss")},
                null,
                null,
                orderBy + " ASC");
        return cursor;
    }


}
