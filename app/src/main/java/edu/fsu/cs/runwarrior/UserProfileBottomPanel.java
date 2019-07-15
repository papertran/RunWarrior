package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UserProfileBottomPanel extends Fragment {
    private OnUserProfilePanelAction mListener;

    public UserProfileBottomPanel() { }

    public static UserProfileBottomPanel newInstance() {
        UserProfileBottomPanel fragment = new UserProfileBottomPanel();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_profile_bottom_panel, container, false);

        root.findViewById(R.id.switch_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickClose();
            }
        });

        root.findViewById(R.id.switch_runs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRuns();
            }
        });

        root.findViewById(R.id.switch_graph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGraph();
            }
        });

        return root;
    }

    private void onClickGraph() {
        if (mListener != null) {
            mListener.onUserProfilePanelGraph();
        }
    }

    private void onClickRuns() {
        if (mListener != null) {
            mListener.onUserProfilePanelRuns();
        }
    }

    private void onClickClose() {
        if (mListener != null) {
            mListener.onUserProfilePanelClose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserProfilePanelAction) {
            mListener = (OnUserProfilePanelAction) context;
        }
        else {
            throw new RuntimeException(context.toString()
              + " must implement OnUserProfilePanelClose");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUserProfilePanelAction {
        void onUserProfilePanelClose();
        void onUserProfilePanelRuns();
        void onUserProfilePanelGraph();
    }
}
