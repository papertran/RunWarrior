package edu.fsu.cs.runwarrior;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MapsActivity extends FragmentActivity implements
        BottomPanelFragment.BottomPanelFragmentListener {
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

    @Override
    public void onAvatarButonClicked() {

    }

    @Override
    public void onQuestButtonClicked() {

    }

    @Override
    public void onStartButonClicked() {

    }
}
