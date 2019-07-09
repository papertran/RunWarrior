package edu.fsu.cs.runwarrior;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MapsActivity extends FragmentActivity implements
        BottomPanelFragment.BottomPanelFragmentListener,
        BottomPanelStartRunFragment.BottomPanelFragmentStartRunListener,
        MapFragment.MapFragmentListener {
    // https://www.youtube.com/watch?v=i22INe14JUc&t=16s
    // How to implement / use interfaces for fragment communication


    // Shared Preferences Keys
    public static final String PREFS_NAME = "RunWarriorUserSettings";
    // Boolean
    public static final String USER_CREATED = "USER_CREATED";
    // String
    public static final String USER_NAME = "USER_NAME";
    // Float
    public static final String USER_WEIGHT = "USER_WEIGHT";
    // Int
    public static final String USER_SEX = "USER_SEX";

    public static final String TRACKING_KEY ="TRACKING_KEY";
    public static final String DISTANCE_KEY = "DISTANCE_KEY";
    public static final String TIME_KEY = "TIME_KEY";

    private final String TAG = MapsActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Shared preferences to check if the the user has created an Avatar or not
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();


        Boolean isCreated = settings.getBoolean(USER_CREATED, false);
        if(!isCreated){
            setContentView(R.layout.initial_login);
            Button registerButton = (Button) findViewById(R.id.registerButton);
            if(Build.VERSION.SDK_INT >= 23){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragmentActivity fragment = new DialogFragmentActivity();
                    fragment.show(getSupportFragmentManager(), "Dialog");
                }
            });
        }
        else {
            setContentView(R.layout.main_ui);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            MapFragment fragment = new MapFragment();
            trans.add(R.id.topPanel, fragment, "MapFragment");

            BottomPanelFragment bottomFragment = new BottomPanelFragment();
            trans.add(R.id.bottomPanel, bottomFragment, "BottomFragment");
            trans.commit();
        }
    }

    @Override
    public void onAvatarButtonClicked() {
        Log.i(TAG, "onAvatarButtonClicked");
        //TODO implement onAvatarButonClicked
    }

    @Override
    public void onQuestButtonClicked() {
        Log.i(TAG, "onQuestButtonClicked");
        //TODO implement onQuestButonClicked
    }

    @Override
    public void onStartButtonClicked() {
        Log.i(TAG, "onStartButtonClicked");
        BottomPanelStartRunFragment fragment = new BottomPanelStartRunFragment();
        String tag = BottomPanelStartRunFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomPanel, fragment, tag).commit();

        // Sending bundle to Mapfragment that will tell it to start tracking the user
        Bundle extras = new Bundle();
        extras.putBoolean(TRACKING_KEY, true);

        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(extras);
        tag = MapFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.topPanel, mapFragment, tag).commit();

    }

    @Override
    public void onEndButtonClicked() {
        Log.i(TAG, "onEndButtonClicked");
        BottomPanelFragment fragment = new BottomPanelFragment();
        String tag = BottomPanelFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomPanel, fragment, tag).commit();

        MapFragment mapFragment = new MapFragment();
        tag = MapFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.topPanel, mapFragment, tag).commit();
    }


    @Override
    public void sendDistance(double distance, int seconds) {
        Bundle extras = new Bundle();
        extras.putDouble(DISTANCE_KEY, distance);
        extras.putInt(TIME_KEY, seconds);


        BottomPanelStartRunFragment fragment = new BottomPanelStartRunFragment();
        fragment.setArguments(extras);
        String tag = BottomPanelStartRunFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomPanel, fragment, tag).commit();

    }
}
