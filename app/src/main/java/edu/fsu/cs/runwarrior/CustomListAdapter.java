package edu.fsu.cs.runwarrior;

import android.content.ContentResolver;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
// Custom list adapter so we can use
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<String[]> data;
    private final LayoutInflater inflator;

    public CustomListAdapter(Context context, ArrayList<String[]> data){
        this.data = data;
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String[] getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null){
            vi = inflator.inflate(R.layout.run_item, null);
        }
        TextView datecol = vi.findViewById(R.id.dateColumn);
        TextView distcol = vi.findViewById(R.id.distanceColumn);
        TextView timecol = vi.findViewById(R.id.timeColumn);

        String[] item = getItem(position);
        datecol.setText(item[0]);
        distcol.setText(item[1] + " meters");
        timecol.setText(item[2]);

        return vi;
    }
}
