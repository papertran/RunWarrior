package edu.fsu.cs.runwarrior;

import android.content.Context;
import android.os.Bundle;
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

    private BottomPanelFragmentListener mListener;
    private ImageButton avatarImageButton;
    private TextView lvlTextVIew;
    private ProgressBar expProgressBar;
    private Button questButon;
    private Button startButton;

    public interface BottomPanelFragmentListener{
        void onStartButonClicked();
        void onQuestButtonClicked();
        void onAvatarButonClicked();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_pannel, container, false);

        avatarImageButton = (ImageButton) rootView.findViewById(R.id.avatarImageButon);
        lvlTextVIew = (TextView) rootView.findViewById(R.id.levelTextView);
        expProgressBar = (ProgressBar) rootView.findViewById(R.id.expProgressBar);
        questButon = (Button) rootView.findViewById(R.id.questButton);
        startButton = (Button) rootView.findViewById(R.id.startButton);

        avatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAvatarButonClicked();
            }
        });


        questButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onQuestButtonClicked();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStartButonClicked();
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
