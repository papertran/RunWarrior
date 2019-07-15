package edu.fsu.cs.runwarrior;


import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment {

    public GraphFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_graph, container, false);

        String[] mproj1 = new String[] { "_ID", RWContentProvider.DISTANCE_RAN };
        String sel1 = null;
        String[] selArgs1 = null;
        String sortOrder1 = "_ID" + " COLLATE LOCALIZED ASC";
        Cursor q = getActivity().getContentResolver().query(RWContentProvider.CONTENT_URI, mproj1, sel1, selArgs1, sortOrder1);

        List<Integer> l1 = new ArrayList<Integer>(); // id
        List<Double> l2 = new ArrayList<Double>(); // distance

        final Spinner sp = view.findViewById(R.id.rate);
        RadioGroup runwalk = view.findViewById(R.id.rw);
        final RadioButton run = view.findViewById(R.id.running);
        final RadioButton walk = view.findViewById(R.id.walking);

        if(q != null) {
            q.moveToFirst();
            int t1;
            double t2;

            for(int i = 0; i < q.getCount(); i++) {
                t1 = q.getInt(q.getColumnIndexOrThrow("_ID"));
                l1.add(t1);

                t2 = q.getDouble(q.getColumnIndexOrThrow(RWContentProvider.DISTANCE_RAN));
                l2.add(t2);

                q.moveToNext();
            }

            q.close();
        }

        GraphView graph1 = (GraphView) view.findViewById(R.id.graph1);
        DataPoint[] dp = new DataPoint[l1.size()];
        graph1.removeAllSeries();

        for(int i = 0; i < l1.size(); i++) {
            dp[i] = new DataPoint(l1.get(i), l2.get(i));
        }

        LineGraphSeries<DataPoint> plot = new LineGraphSeries<>(dp);
        graph1.addSeries(plot);
        graph1.setTitle("Distance by Session");
        graph1.getGridLabelRenderer().setHorizontalLabelsVisible(true);
        graph1.getGridLabelRenderer().setHorizontalAxisTitle("Session");
        graph1.getGridLabelRenderer().setVerticalLabelsVisible(true);
        graph1.getGridLabelRenderer().setVerticalAxisTitle("Distance");


        if(run.isChecked() == false && walk.isChecked() == false)
            sp.setEnabled(false);

        runwalk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] rnwk;
                ArrayAdapter<String> rw;
                if(run.isChecked()) {
                    rnwk = view.getResources().getStringArray(R.array.rng);
                    rw = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rnwk);
                    rw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(rw);
                    sp.setEnabled(true);
                }
                else if(walk.isChecked()) {
                    rnwk = view.getResources().getStringArray(R.array.wkg);
                    rw = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rnwk);
                    rw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(rw);
                    sp.setEnabled(true);
                }
            }
        });

        Button sbutton = (Button) view.findViewById(R.id.submitButton);
        SharedPreferences settings = getActivity().getSharedPreferences(MapsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        double weight = settings.getFloat(MapsActivity.USER_WEIGHT, 0);
        final EditText e1 = view.findViewById(R.id.weightEntry);
        e1.setText(Double.toString(weight));

        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean error = false;          // ie, no errors

                if(run.isChecked() == false && walk.isChecked() == false || e1.getText().toString().equals(""))
                    error = true;
                else
                    error = false;

                if(error == true) {
                    Toast.makeText(getActivity(), "Error: Please make sure all necessary data is filled in; you must " +
                            "select the exercise you most often do, and enter your weight to compute the callories burned.", Toast.LENGTH_SHORT).show();
                }
                else {
                    double multiplier = 1;
                    if(sp.isEnabled() == true)
                    {
                        if(run.isChecked())
                            multiplier = getMultiplier(sp.getSelectedItemPosition(), 'r');
                        else if(walk.isChecked())
                            multiplier = getMultiplier(sp.getSelectedItemPosition(), 'w');
                    }

                    String[] mproj2 = new String[] { "_ID", RWContentProvider.TIME_ELAPSED };
                    String sel2 = null;
                    String[] selArgs2 = null;
                    String sortOrder2 = "_ID" + " COLLATE LOCALIZED ASC";
                    Cursor q2 = getActivity().getContentResolver().query(RWContentProvider.CONTENT_URI, mproj2, sel2, selArgs2, sortOrder2);


                    List<Integer> l3 = new ArrayList<Integer>();     // ID
                    List<Integer> l4 = new ArrayList<Integer>();     // Time Elapsed (in minutes)

                    if(q2 != null) {
                        q2.moveToFirst();
                        int t3;
                        String t4;

                        for(int i = 0; i < q2.getCount(); i++) {
                            t3 = q2.getInt(q2.getColumnIndexOrThrow("_ID"));
                            l3.add(t3);

                            t4 = q2.getString(q2.getColumnIndexOrThrow(RWContentProvider.TIME_ELAPSED));
                            String hours = t4.substring(0, 2);
                            String minutes = t4.substring(3, 5);
                            String seconds = t4.substring(7);
                            int x = Integer.parseInt(hours);
                            int y = Integer.parseInt(minutes);
                            int z = Integer.parseInt(seconds);

                            y = y + (x*60) + (z/60);
                            l4.add(y);

                            q2.moveToNext();
                        }

                        q2.close();
                    }

                    double wgt = Double.parseDouble(e1.getText().toString());
                    GraphView graph2 = (GraphView) view.findViewById(R.id.graph2);
                    DataPoint[] dp2 = new DataPoint[l3.size()];
                    graph2.removeAllSeries();

                    for(int i = 0; i < l4.size(); i++) {
                        dp2[i] = new DataPoint(l3.get(i), (l4.get(i)*.0175*multiplier));
                        // the y-point is a calculation to derive the amount of calories burned
                            // calculation from https://www.hss.edu/conditions_burning-calories-with-exercise-calculating-estimated-energy-expenditure.asp?fbclid=IwAR3eY2ru-ypCfpPLzGQgSq951CHVEOVz97cbc0MoHWxcHEnZTRAiaYSEaaU
                    }

                    LineGraphSeries<DataPoint> sr = new LineGraphSeries<>(dp2);

                    graph2.addSeries(sr);
                    graph2.setTitle("Calories Burned by Session");
                    graph2.getGridLabelRenderer().setHorizontalLabelsVisible(true);
                    graph2.getGridLabelRenderer().setHorizontalAxisTitle("Session");
                    graph2.getGridLabelRenderer().setVerticalLabelsVisible(true);
                    graph2.getGridLabelRenderer().setVerticalAxisTitle("Calories Burned");
                }
            }
        });


        return view;
    }

    double getMultiplier(int x, char which) {
        double multiplier = 0;

        if(which == 'r')
        {
            switch(x) {
                case 0: {
                    multiplier = 8;
                    break;
                }
                case 1: {
                    multiplier = 9;
                    break;
                }
                case 2: {
                    multiplier = 10;
                    break;
                }
                case 3: {
                    multiplier = 11;
                    break;
                }
                case 4: {
                    multiplier = 11.5;
                    break;
                }
                case 5: {
                    multiplier = 12.5;
                    break;
                }
                case 6: {
                    multiplier = 13.5;
                    break;
                }
                case 7: {
                    multiplier = 14;
                    break;
                }
                case 8: {
                    multiplier = 15;
                    break;
                }
                case 9: {
                    multiplier = 16;
                    break;
                }
                case 10: {
                    multiplier = 18;
                    break;
                }
                case 11: {
                    multiplier = 15;
                    break;
                }
            }
        }
        else if(which == 'w')
        {
            switch(x) {
                case 0: {
                    multiplier = 2.5;
                    break;
                }
                case 1: {
                    multiplier = 3;
                    break;
                }
                case 2: {
                    multiplier = 3.5;
                    break;
                }
                case 3: {
                    multiplier = 4;
                    break;
                }
                case 4: {
                    multiplier = 4.5;
                    break;
                }
                case 5: {
                    multiplier = 6.5;
                    break;
                }
            }
        }

        return multiplier;
    }


}
