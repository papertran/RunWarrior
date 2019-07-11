package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_panel, container, false);

        avatarImageButton = (ImageButton) rootView.findViewById(R.id.avatarImageButton);
//        lvlTextVIew = (TextView) rootView.findViewById(R.id.levelTextViewOld);
//        expProgressBar = (ProgressBar) rootView.findViewById(R.id.expProgressBarOld);
        questButton = (Button) rootView.findViewById(R.id.questButton);
        startButton = (Button) rootView.findViewById(R.id.startButton);

        avatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: onAvatarButtonClicked() called");
                mListener.onAvatarButtonClicked();
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
}
