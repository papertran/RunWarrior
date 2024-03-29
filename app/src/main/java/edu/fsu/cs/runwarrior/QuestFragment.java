package edu.fsu.cs.runwarrior;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.charset.Charset;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestFragment extends Fragment {
    ListView Quest;

    public QuestFragment() {
        // Required empty public constructor
    }

    int level, time, requiredLvlExp;
    float m;
    int initalexp,initaltime;
    double initialmiles,InMiles;  //gets initial experience
    String lvlString;
    String minute;
    String kilometer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_quest, container, false);
        SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        time=pref.getInt("Time",-1);
        requiredLvlExp=pref.getInt("Requiredxp",-1);
        m=pref.getFloat("Miles",-1);
        lvlString=pref.getString("levelup","-1");
       // Toast.makeText(getActivity(), time+ " "+requiredLvlExp,Toast.LENGTH_LONG).show();
        if (time==-1||requiredLvlExp==-1||m==-1||lvlString=="-1") {
            time = 10;
            m = 1000;
            requiredLvlExp = 200;
            lvlString="0";
            Log.i("TAG",lvlString);
        }
        Log.i("TAG2",lvlString);
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            minute=bundle.getString("mm","null");
           // lvlString = bundle.getString("lvl", "-1");
            initaltime=Integer.parseInt(minute);
            Log.i("TAG3",lvlString);
            initalexp=bundle.getInt("xp",-1);
            initialmiles=bundle.getFloat("distance",-1);
            // Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
        }
        else
        {
            initalexp=0;
            initialmiles=0;
            //lvlString="0";
         //   time = 10;
         //   miles = 1000;
         //   requiredLvlExp = 200;
           // initalexp=0;
           // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getActivity(),requiredLvlExp,Toast.LENGTH_LONG).show();
        Log.i("miles", "onCreateView: " + initialmiles);
        Log.i("Miles", "onCreateView: " + m);

        Quest = (ListView) v.findViewById(R.id.selection_list);
        if (initialmiles >= m) {
            do {
                initalexp += 100;
                m += 1000;
                Log.i("testing", "onCreateView: " + initalexp);
            }while((initialmiles>=m));
            //update GetExperience
        }
        if (initaltime >= time) {
            do {
                initalexp += 100;
                time = Increasetime(time);
            }while (initaltime>=time);
            //update GetExperience
        }
        if (initalexp >= requiredLvlExp) //comparing experience with required lvlexp;
        {
            Log.i("TAG4",lvlString);
           // Toast.makeText(getActivity(),"Level Up",Toast.LENGTH_LONG);
            do {
                lvlString = levelup(Integer.parseInt(lvlString));
                requiredLvlExp = lvlExp(requiredLvlExp);
                Log.i("level","yay"+lvlString);
            }while ((initalexp>=requiredLvlExp));

        }
        update(initalexp);
        String quest1 = Double.toString(m/1000);
        String quest2 = Double.toString(time);
        String[] quest = {"Run a total of " + quest1 + " Kilometer", "Run for " + quest2 + " minutes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, quest); //creates adaptor
        Quest.setAdapter(adapter); //prints listview
      //  TextView updatelvl = (TextView) getActivity().findViewById(R.id.levelTextViewOld);
//       // updatelvl.setText("Level "+lvlString); //finds textview of the lvl up and then modifies it
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext()); //creating preference
//        SharedPreferences.Editor editor = preferences.edit();
        SharedPreferences prefs= getActivity().getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        Log.i("QUESTFRAGMENT", "onCreateView: " + lvlString);
        prefs.edit().putInt("Requiredxp", requiredLvlExp).apply();
        prefs.edit().putInt("Time", time).apply();
        prefs.edit().putFloat("Miles", m).apply();
        prefs.edit().putString("levelup",lvlString).apply();
        //Toast.makeText(getActivity(),requiredLvlExp,Toast.LENGTH_LONG).show();
        //editor.commit(); //store the sharepreference



        return v;

    }


    public String levelup(int x) //simple helper functions
    {
        return Integer.toString(x + 1);
    }

    public int lvlExp(int x) {
        return x + 200;
    }

    public int Increasetime(int x) {
        return x + 2;
    }
    public void update(int x) {
        ContentValues mupdate = new ContentValues();
        mupdate.put(RWContentProvider.EXP_EARNED, x);

        String mSelectionClause;
        String[] mSelectionArgs;

        mSelectionClause =
                RWContentProvider.EXP_EARNED + " = ? ";

        mSelectionArgs = new String[]{String.valueOf(x)};
        int updated = 0;
        updated = getActivity().getContentResolver().update(RWContentProvider.CONTENT_URI, mupdate, mSelectionClause, mSelectionArgs);
    }
}


