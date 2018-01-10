package axeleration.com.ex4_homework;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false); // convert fragment to view.
        ListView listView = view.findViewById(R.id.listView);   // list view.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // set onclick on each item from list view.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager(); //  get the fragment manager.
                FragmentTransaction transaction = manager.beginTransaction();   // get the fragment transaction.
                Fragment fragment = new DataFragment(); // New date fragment.
                fragment.setArguments(setUpBundle());   // set arguments to the fragment.
                if(getActivity().findViewById(R.id.data) != null)   // if its tablet mode - landscape.
                    transaction.replace(R.id.data, fragment);
                else    // its mobile mode - portrait.
                    transaction.replace(R.id.listLayout, fragment);
                transaction.addToBackStack(null);   // add to the stack on back functionality.
                transaction.commit();   // commit the fragment transaction.
            }
        });
        MyAdapter adapter = new MyAdapter(getActivity(), ((MainActivity)getActivity()).getCursor());
        listView.setAdapter(adapter);
        return view;
    }

    private Bundle setUpBundle() {
        Cursor cursor = ((MainActivity)getActivity()).getCursor();  // get the cursor from main activity.
        Bundle bundle = new Bundle();       // new bundle.
        String contactName = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        String phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        String imageUri = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)));
        bundle.putString("name", contactName);  // set name to the bundle.
        bundle.putString("phone", phoneNumber); // set phone to the bundle.
        bundle.putString("img", imageUri);  // set image to the bundle.
        return bundle;
    }



}
