package example.com.ex2_homework;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {

    private LayoutInflater inflater;


    MyAdapter(Context context, Cursor c) {
        super(context, c, false);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.item_row, parent,false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final int _id = cursor.getInt(cursor.getColumnIndex(Constants.ASU._ID));
        TextView latitude = view.findViewById(R.id.latitude);
        TextView longitude = view.findViewById(R.id.longitude);
        TextView date = view.findViewById(R.id.date);
        TextView asu = view.findViewById(R.id.asu);
        ImageButton btn = view.findViewById(R.id.img_delete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).removeRow(_id);
            }
        });

        StringBuilder builder;
        builder = new StringBuilder("Lat:\n");
        builder.append(cursor.getString(cursor.getColumnIndex(Constants.ASU.LATITUDE)));
        latitude.setText(builder);

        builder = new StringBuilder("Long:\n");
        builder.append(cursor.getString(cursor.getColumnIndex(Constants.ASU.LONGITUDE)));
        longitude.setText(builder);

        builder = new StringBuilder(cursor.getString(cursor.getColumnIndex(Constants.ASU.DATE)));
        date.setText(builder);

        builder = new StringBuilder("asu: ");
        builder.append(cursor.getInt(cursor.getColumnIndex(Constants.ASU.ASU)));
        asu.setText(builder);

        setViewColor(view, cursor.getInt(cursor.getColumnIndex(Constants.ASU.ASU)));
    }

    private void setViewColor(View view, int asu) {
        if(asu >= 0 && asu <= 10) {
            view.setBackgroundColor(Color.RED);
        } else if(asu > 10 && asu <= 20) {
            view.setBackgroundColor(Color.YELLOW);
        } else if(asu > 20 && asu <= 31){
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
