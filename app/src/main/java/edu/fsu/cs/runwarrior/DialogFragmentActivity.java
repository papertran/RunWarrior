package edu.fsu.cs.runwarrior;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class DialogFragmentActivity extends DialogFragment {

    private EditText nameEditText;
    private EditText weightEditText;
    private RadioGroup avatarRadioGroup;

    private static final String TAG = "DialogFragmentActivity";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.register_avatar_dialog_fragment, null);
        nameEditText = view.findViewById(R.id.nameEditText);
        weightEditText = view.findViewById(R.id.weightEditText);
        avatarRadioGroup = view.findViewById(R.id.avatarRadioGroup);

        // TODO fix Avatar images
        builder.setView(view)
                .setTitle("Create your running Avatar!")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String name = nameEditText.getText().toString();
                        float weight = Float.parseFloat(weightEditText.getText().toString());
                        int radiochoice = avatarRadioGroup.getCheckedRadioButtonId();
                        View radioButton = avatarRadioGroup.findViewById(radiochoice);
                        int selectedAvatar = avatarRadioGroup.indexOfChild(radioButton);

                        SharedPreferences settings = getContext().
                                getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(MapsActivity.USER_NAME, name);
                        editor.putFloat(MapsActivity.USER_WEIGHT, weight);
                        editor.putInt(MapsActivity.USER_SEX, selectedAvatar);
                        editor.putBoolean(MapsActivity.USER_CREATED, true);
                        editor.apply();

                        Log.i(TAG, "onClick: name is " + name);
                        Log.i(TAG, "onClick: weight is " + weight);
                        Log.i(TAG, "onClick: sex choice is " + selectedAvatar);

                        Intent restartMain = new Intent(getContext(), MapsActivity.class);
                        startActivity(restartMain);
                    }
                });
        return builder.create();
    }
}
