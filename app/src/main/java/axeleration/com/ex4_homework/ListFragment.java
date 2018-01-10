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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = new DataFragment();
                fragment.setArguments(setUpBundle());
                if(getActivity().findViewById(R.id.data) != null)
                    transaction.replace(R.id.data, fragment);
                else
                    transaction.replace(R.id.listLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        MyAdapter adapter = new MyAdapter(getActivity(), ((MainActivity)getActivity()).getCursor());
        listView.setAdapter(adapter);
        return view;
    }

    private Bundle setUpBundle() {
        Cursor cursor = ((MainActivity)getActivity()).getCursor();
        Bundle bundle = new Bundle();
        String contactName = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
        String phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        String imageUri = (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)));
        bundle.putString("name", contactName);
        bundle.putString("phone", phoneNumber);
        bundle.putString("img", imageUri);
        return bundle;
    }



}
