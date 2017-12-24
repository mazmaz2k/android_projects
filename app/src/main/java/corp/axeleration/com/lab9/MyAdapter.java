package corp.axeleration.com.lab9;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public MyAdapter(Context context, Cursor c) {
        super(context, c);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.my_layout,null,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView number = view.findViewById(R.id.number);
        String num = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
        number.setText(num);
    }
}
