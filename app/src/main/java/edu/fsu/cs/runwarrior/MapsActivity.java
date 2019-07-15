package edu.fsu.cs.runwarrior;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements
  BottomPanelFragment.BottomPanelFragmentListener,
  BottomPanelStartRunFragment.BottomPanelFragmentStartRunListener,
  MapFragment.MapFragmentListener,
  UserProfileBottomPanel.OnUserProfilePanelAction {
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

    public static final String TRACKING_KEY = "TRACKING_KEY";
    public static final String DISTANCE_KEY = "DISTANCE_KEY";
    public static final String TIME_KEY = "TIME_KEY";
    public static final String AVATAR_IMAGE = "AVATAR_IMAGE";

    private static final int GET_FROM_GALLERY = 3;

    private final String TAG = MapsActivity.class.getCanonicalName();
    private Uri imageUri;

    public Uri getImageUri() {
        return imageUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Shared preferences to check if the the user has created an Avatar or not
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();

        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
              PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
              PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
              PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            String[] perms = permissions.toArray(new String[]{});
            if (perms.length > 0) {
                ActivityCompat.requestPermissions(this, perms, 0);
            }
        }

        Boolean isCreated = settings.getBoolean(USER_CREATED, false);
        if (!isCreated) {
            setContentView(R.layout.initial_login);
            //https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android

            Button uploadButton = findViewById(R.id.uploadImageButton);
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT,
                                        MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                GET_FROM_GALLERY);
                    } else {

                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                GET_FROM_GALLERY);
                    }
                }
            });

            Button registerButton = (Button) findViewById(R.id.submitButton);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (createCharacter()) {
                        restartActivity();
                    }
                }
            });
        }
        // user already exists/is logged in/has been created
        else {
            // load avatar image resource
            this.imageUri = Uri.parse(settings.getString(AVATAR_IMAGE, ""));

            // initialize UI to main view and setup fragment controller
            setContentView(R.layout.main_ui);
            loadFragmentPair(
              new MapFragment(), "MapFragment",
              new BottomPanelFragment(), "BottomPanelFragment"
            );
        }
    }

    // set the top and bottom panels to specified fragment arguments, allow any instances of fragments from Fragment-extended classes as args
    private <T extends Fragment, U extends Fragment> void loadFragmentPair(T top, String topTag, U bottom, String bottomTag) {
        // set top panel and allow back button
        getSupportFragmentManager()
          .beginTransaction()
          // set top panel to `top` argument
          .replace(R.id.topPanel, top, topTag)
          .addToBackStack(null) // support back button
          // set bottom panel to `bottom` argument
          .replace(R.id.bottomPanel, bottom, bottomTag)
          .addToBackStack(null)// support back button
          .commit();
    }

    private UserProfile getUserProfileFragment() {
       return (UserProfile) getSupportFragmentManager().findFragmentById(R.id.topPanel);
    }

    @Override
    public void onUserProfilePanelRuns() {
        getUserProfileFragment().showRuns();
    }

    @Override
    public void onUserProfilePanelGraph() {
        getUserProfileFragment().showGraph();
    }

    @Override
    public void onUserProfilePanelClose() {
        loadFragmentPair(
          new MapFragment(), "MapFragment",
          new BottomPanelFragment(), "BottomPanelFragment"
        );
    }

    @Override
    public void onAvatarButtonClicked() {
        Log.i(TAG, "onAvatarButtonClicked");

        loadFragmentPair(
          new UserProfile(), "UserProfileFragment",
          new UserProfileBottomPanel(), "UserProfileBottomPanelFragment"
        );
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
    public void onEndButtonClicked(ContentValues values) {
        Log.i(TAG, "onEndButtonClicked");
        BottomPanelFragment fragment = new BottomPanelFragment();
        String tag = BottomPanelFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomPanel, fragment, tag).commit();

        MapFragment mapFragment = new MapFragment();
        tag = MapFragment.class.getCanonicalName();
        getSupportFragmentManager().beginTransaction().replace(R.id.topPanel, mapFragment, tag).commit();


        // Just example to see data passed into here
        int exp = values.getAsInteger(RWContentProvider.EXP_EARNED);
        float distance = values.getAsFloat(RWContentProvider.DISTANCE_RAN);
        String timeElapsed = values.getAsString(RWContentProvider.TIME_ELAPSED);
        String date = values.getAsString(RWContentProvider.DATE);

        Log.i(TAG, "onEndButtonClicked: exp = " + exp);
        Log.i(TAG, "onEndButtonClicked: distance = " + distance);
        Log.i(TAG, "onEndButtonClicked: time = " + timeElapsed);
        Log.i(TAG, "onEndButtonClicked: date = " + date);
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

    private boolean createCharacter() {
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText weightEditText = findViewById(R.id.weightEditText);

        String name = nameEditText.getText().toString();
        float weight = Float.parseFloat(weightEditText.getText().toString());

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        SharedPreferences settings = getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MapsActivity.USER_NAME, name);
        editor.putFloat(MapsActivity.USER_WEIGHT, weight);
        editor.putBoolean(MapsActivity.USER_CREATED, true);
        if (imageUri == null) {
            imageUri = Uri.parse("");
        }
        editor.putString(AVATAR_IMAGE, imageUri.toString());

        editor.apply();
        return true;
    }

    private void restartActivity() {
        Intent restartMain = new Intent(this, MapsActivity.class);
        startActivity(restartMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            if (imageUri == null){
                imageUri = Uri.parse("");
            }
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                ImageView avatarPreview = findViewById(R.id.avatarImagePreview);
                avatarPreview.setImageBitmap(bMapScaled);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
