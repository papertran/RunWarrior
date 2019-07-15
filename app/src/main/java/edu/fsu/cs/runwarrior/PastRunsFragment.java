package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class PastRunsFragment extends Fragment {
    public PastRunsFragment() { }

    public static PastRunsFragment newInstance() {
        PastRunsFragment fragment = new PastRunsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_past_runs, container, false);

        // Collect all past runs into list
        ArrayList<String> pastRunsData = new ArrayList<>();
        Cursor c = getActivity()
          .getApplicationContext()
          .getContentResolver()
          .query(RWContentProvider.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); ++i, c.moveToNext()) {
                pastRunsData.add("Run #" + c.getString(1) + " - Ran " + c.getString(2) + " meters in " + c.getString(3) + " on " + c.getString(5));
            }
        }
        c.close();

        // Set adapter to listview
        ArrayAdapter<String> pastRunsViewAdapter = new ArrayAdapter<String>(
          getActivity(),
          android.R.layout.simple_list_item_1,
          pastRunsData.toArray(new String[0])
        );
        ListView pastRunsView = root.findViewById(R.id.userProfile_pastRuns_list);
        pastRunsView.setAdapter(pastRunsViewAdapter);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
