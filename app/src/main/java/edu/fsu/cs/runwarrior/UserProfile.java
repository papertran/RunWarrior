package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserProfile extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private OnFragmentInteractionListener mListener;

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

        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        return root;
    }

    private <T extends Fragment> void setWidget(T f, String tag) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.row_widget, f, tag).addToBackStack(null);
        trans.commit();
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//              + " must implement OnUserProfilePanelClose");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    void showGraph() {
        Log.i("switch to graph", "meme");
        setWidget(new GraphFragment(), "Graph");
    }

    void showRuns() {
        Log.i("switch to runs", "meme");
        //setWidget(new GraphFragment(), "Graph");
    }
}
