package edu.fsu.cs.runwarrior;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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

    private GoogleMap mMap;

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
