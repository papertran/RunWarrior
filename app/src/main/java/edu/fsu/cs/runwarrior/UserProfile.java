package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.content.SharedPreferences;
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

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ((TextView)root.findViewById(R.id.userProfile_name)).setText(userName);
        ((TextView)root.findViewById(R.id.userProfile_weight)).setText(String.valueOf(userWeight));
        ((TextView)root.findViewById(R.id.userProfile_totalMilesRan)).setText("0");
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
