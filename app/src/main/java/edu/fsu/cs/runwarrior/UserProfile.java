package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class UserProfile extends Fragment {
    public UserProfile() {}

    public static UserProfile newInstance() {
        UserProfile fragment = new UserProfile();
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
        SharedPreferences settings = getActivity().getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String userName = settings.getString(MapsActivity.USER_NAME, "Name not set");
        float userWeight = settings.getFloat(MapsActivity.USER_WEIGHT, 0);
        String userAvatar = settings.getString(MapsActivity.AVATAR_IMAGE, null);
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

        ((TextView)root.findViewById(R.id.userProfile_name)).setText(userName);
        ((TextView)root.findViewById(R.id.userProfile_weight)).setText(String.valueOf(userWeight) + " lbs");
        ((TextView)root.findViewById(R.id.userProfile_totalDistanceRan)).setText(String.valueOf(userDistanceRan) + " m");
        ((ImageButton)root.findViewById(R.id.userProfile_avatar)).setImageURI(Uri.parse(userAvatar));

        showRuns();

        return root;
    }

    private <T extends Fragment> void setWidget(T f, String tag) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.userProfile_row_widget, f, tag).addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    void showGraph() {
        setWidget(new GraphFragment(), "Graph");
    }

    void showRuns() {
        setWidget(new PastRunsFragment(), "Graph");
    }
}
