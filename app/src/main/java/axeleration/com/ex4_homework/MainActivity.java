package axeleration.com.ex4_homework;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final int CONTACTS_REQUEST_CODE = 1234;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {    // Check for permissions.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, CONTACTS_REQUEST_CODE); // Request permission if not exists.
        }
        else {  // Have all the permissions.
            setTheData();  // Set the cursor of the contacts and setup the main fragment.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CONTACTS_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {   // Permissions granted.
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    setTheData(); // Set the cursor of the contacts and setup the main fragment.
                } else {    // Permissions denied.
                    Toast.makeText(this, "This application may not work properly!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void setTheData() { // Set the cursor of the contacts and setup the main fragment.
        this.cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

        android.app.FragmentManager manager = getFragmentManager(); // Get fragment manager.
        android.app.FragmentTransaction transaction = manager.beginTransaction(); // setup the fragment transaction.

        Fragment fragment = new ListFragment(); // Init main fragment.

        transaction.replace(R.id.listLayout, fragment); // Put the fragment into frame layout.
        transaction.commit();   // commit the changes.
    }

    public Cursor getCursor() {
        return this.cursor;
    }   // return the cursor.

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null) {
            this.cursor.close();
        }
    }
}
