package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BottomPanelStartRunFragment extends Fragment {

    private final String TAG = BottomPanelStartRunFragment.class.getCanonicalName();
    private BottomPanelFragmentStartRunListener mListener;
    TextView distanceTextVIew;
    TextView timeTextView;
    TextView expTextView;
    Button endButton;

    public interface BottomPanelFragmentStartRunListener{
        void onEndButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_panel_start_run, container, false);

        distanceTextVIew = (TextView) rootView.findViewById(R.id.distanceTextView);
        timeTextView = (TextView) rootView.findViewById(R.id.timeElapsedTextVIew);
        expTextView = (TextView) rootView.findViewById(R.id.expTextView);
        endButton = (Button) rootView.findViewById(R.id.endButton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onEndButtonClicked called");
                mListener.onEndButtonClicked();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BottomPanelFragmentStartRunListener){
            mListener = (BottomPanelFragmentStartRunListener) context;
        } else{
            throw new RuntimeException(context.toString() + " must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
