package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserProfile extends Fragment {
    public UserProfile() {}
    void showGraph() { setWidget(new GraphFragment(), "Graph"); }
    void showRuns() { setWidget(new PastRunsFragment(), "Graph"); }

    // swap fragment container to either graph or past runs fragment
    private <T extends Fragment> void setWidget(T f, String tag) {
        getChildFragmentManager()
          .beginTransaction()
          // will be either PastRunsFragment or GraphFragment
          .replace(R.id.userProfile_row_widget, f, tag)
          .addToBackStack(null) // allow back button
          .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences settings = getActivity().getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String userName = settings.getString(MapsActivity.USER_NAME, "Name not set");
        String userAvatar = settings.getString(MapsActivity.AVATAR_IMAGE, null);
        float userWeight = settings.getFloat(MapsActivity.USER_WEIGHT, 0);
        float userDistanceRan = 0;

        // Collect all past runs into list
        Cursor c = getActivity()
          .getApplicationContext()
          .getContentResolver()
          .query(RWContentProvider.CONTENT_URI, null, null, null, null);

        c.moveToFirst();
        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); ++i, c.moveToNext()) {
                userDistanceRan += Float.valueOf(c.getString(2));
            }
        }
        c.close();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // set TextViews' contents with user-specific details
        ((TextView)root.findViewById(R.id.userProfile_name)).setText(userName);
        ((TextView)root.findViewById(R.id.userProfile_weight)).setText(userWeight + " lbs");
        ((TextView)root.findViewById(R.id.userProfile_totalDistanceRan)).setText(userDistanceRan + " m");
        ((ImageButton)root.findViewById(R.id.userProfile_avatar)).setImageURI(Uri.parse(userAvatar));

        // initialize first fragment, PastRunsFragment
        showRuns();

        return root;
    }

    @Override public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }
    @Override public void onAttach(Context context) { super.onAttach(context); }
    @Override public void onDetach() { super.onDetach(); }
}
