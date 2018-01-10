package axeleration.com.ex4_homework;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DataFragment extends Fragment {

    public DataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_data, container, false);    // set view from layout.
        TextView name = view.findViewById(R.id.fullName);
        TextView number = view.findViewById(R.id.phoneNumber);
        ImageView img = view.findViewById(R.id.imageView);
        String imgUri = getArguments().getString("img");    // setup image uri from fragment arguments, null if the key not exist.
        name.setText(getArguments().getString("name"));
        number.setText(getArguments().getString("phone"));
        if(imgUri != null) {    // if null make default image else setup the user image.
            img.setImageURI(Uri.parse(imgUri));     // set the image into image view.
        }
        return view;
    }
}
