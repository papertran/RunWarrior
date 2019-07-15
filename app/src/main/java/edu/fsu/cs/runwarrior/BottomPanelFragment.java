package edu.fsu.cs.runwarrior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BottomPanelFragment extends Fragment {

    private final String TAG = BottomPanelFragment.class.getCanonicalName();
    
    private BottomPanelFragmentListener mListener;
    private ImageButton avatarImageButton;
    private TextView lvlTextVIew;
    private ProgressBar expProgressBar;
    private Button questButton;
    private Button startButton;

    public interface BottomPanelFragmentListener{
        void onStartButtonClicked();
        void onQuestButtonClicked();
        void onAvatarButtonClicked();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_panel, container, false);
        avatarImageButton = (ImageButton) rootView.findViewById(R.id.avatarImageButton);
        Uri imageUri = ((MapsActivity)getActivity()).getImageUri();
        setAvatarImage(imageUri);
//        lvlTextVIew = (TextView) rootView.findViewById(R.id.levelTextViewOld);
//        expProgressBar = (ProgressBar) rootView.findViewById(R.id.expProgressBarOld);

        questButton = (Button) rootView.findViewById(R.id.questButton);
        startButton = (Button) rootView.findViewById(R.id.startButton);

        avatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onAvatarButtonClicked() called");
            }
        });
        avatarImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        v.setAlpha(1);
                        return true;
                    case MotionEvent.ACTION_DOWN:
                        v.setAlpha((float) .5);
                        mListener.onAvatarButtonClicked();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        return false;
                }
                return false;
            }
        });

        questButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onQuestButtonClicked() called");
                mListener.onQuestButtonClicked();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onStartButtonClicked() called");
                mListener.onStartButtonClicked();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BottomPanelFragmentListener){
            mListener = (BottomPanelFragmentListener) context;
        } else{
            throw new RuntimeException(context.toString() + " must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
    public void setAvatarImage(Uri imageuri) {
        Bitmap bitmap = null;
        if (imageuri.toString().equals("")){
//            bitmap = Bitmap.
            avatarImageButton.setImageDrawable(getResources().getDrawable(R.drawable.avatarmale));
            return;
        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageuri);
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            avatarImageButton.setImageBitmap(bMapScaled);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
