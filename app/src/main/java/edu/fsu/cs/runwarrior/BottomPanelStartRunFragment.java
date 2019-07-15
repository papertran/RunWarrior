package edu.fsu.cs.runwarrior;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

import java.sql.Time;
import java.util.Calendar;

public class BottomPanelStartRunFragment extends Fragment {

    private final String TAG = BottomPanelStartRunFragment.class.getCanonicalName();
    private BottomPanelFragmentStartRunListener mListener;
    private TextView distanceTextVIew;
    private TextView timeTextView;
    private TextView expTextView;
    private Button endButton;


    // Values
    private double distance = 0;
    private double kmDistance = 0;
    private int exp = 0;
    private int timeSeconds = 0;
    private java.sql.Time mTime;

    public interface BottomPanelFragmentStartRunListener{
        void onEndButtonClicked(ContentValues values);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if(extras != null){
            distance = extras.getDouble(MapsActivity.DISTANCE_KEY, 0);
            kmDistance = distance / 1000;
            timeSeconds = extras.getInt(MapsActivity.TIME_KEY, 0);
            exp = (int)(distance / 10);

            int hour = (int)(timeSeconds / 3600);
            int minutes = (int)((timeSeconds%3600) / 60);
            int seconds = ((timeSeconds%3600) % 60);
            mTime = new Time(hour, minutes,seconds);


        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_panel_start_run, container, false);

        distanceTextVIew = (TextView) rootView.findViewById(R.id.distanceTextView);
        timeTextView = (TextView) rootView.findViewById(R.id.timeElapsedTextVIew);
        expTextView = (TextView) rootView.findViewById(R.id.expTextView);
        endButton = (Button) rootView.findViewById(R.id.endButton);

        if(distance != 0 || mTime != null) {
            distanceTextVIew.setText(String.format("%.3f", kmDistance) + "km");
            timeTextView.setText(mTime.toString());
            expTextView.setText("" + exp);
        }



        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onEndButtonClicked called");

                Cursor mCursor = getContext().getContentResolver().query(RWContentProvider.CONTENT_URI,
                        null,null,null,null);


                // https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
                java.util.Calendar cal = Calendar.getInstance();
                java.util.Date utilDate = new java.util.Date();
                cal.setTime(utilDate);

                // Values to store in database
                java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime());
                int runSession = mCursor.getCount() + 1;
                // TIME_ELAPSED is mTIme
                // EXP_EARNED is exp
                // DISTANCE_RAN is distance

                ContentValues contentValues = new ContentValues();
                contentValues.put(RWContentProvider.RUN_SESSION, runSession);
                contentValues.put(RWContentProvider.DISTANCE_RAN, (float)distance);
                contentValues.put(RWContentProvider.TIME_ELAPSED, mTime.toString());
                contentValues.put(RWContentProvider.EXP_EARNED, exp);
                contentValues.put(RWContentProvider.DATE, sqlDate.toString());
                getContext().getContentResolver().insert(RWContentProvider.CONTENT_URI, contentValues);

                mListener.onEndButtonClicked(contentValues);

                // This checks if the content values were inserted correctly, they were
//                mCursor = getContext().getContentResolver().query(RWContentProvider.CONTENT_URI,
//                        null,null,null,null);
//
//                mCursor.moveToLast();
//                Log.i(TAG, "onClick: run_session = "
//                        + mCursor.getString(mCursor.getColumnIndex(RWContentProvider.RUN_SESSION)) );
//
//                Log.i(TAG, "onClick: distance_ran = "
//                        + mCursor.getString(mCursor.getColumnIndex(RWContentProvider.DISTANCE_RAN)) );
//
//                Log.i(TAG, "onClick: Time_elapsed = "
//                        + mCursor.getString(mCursor.getColumnIndex(RWContentProvider.TIME_ELAPSED)) );
//
//                Log.i(TAG, "onClick: EXP_earned = "
//                        + mCursor.getString(mCursor.getColumnIndex(RWContentProvider.EXP_EARNED)) );
//
//                Log.i(TAG, "onClick: DATE = "
//                        + mCursor.getString(mCursor.getColumnIndex(RWContentProvider.DATE)) );


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
