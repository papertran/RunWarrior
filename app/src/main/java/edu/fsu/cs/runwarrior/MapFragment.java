package edu.fsu.cs.runwarrior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getCanonicalName();

    // https://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    // Map Variables
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocation;
    private Boolean startTracking = false;
    private LatLng currentLoc;

    // Location Updates Variables
    private final long minTime = 5000;     // 5 Seconds
    private final long minDistance = 10;   // 10 meters;

    Timer timer = new Timer();

    // Values to send to send to update the BottomPanelStartRunFragment
    private MapFragmentListener mListener;
    private double distance = 0;
    private int seconds = 0;
    public interface MapFragmentListener{
        void sendDistance(double distance, int seconds);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        try{
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch(SecurityException e){
            e.printStackTrace();
        }

        Bundle extras = getArguments();
        if(extras != null){
            startTracking = extras.getBoolean(MapsActivity.TRACKING_KEY, false);
        }
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                // For showing a move to my location button
                try {
                    mGoogleMap.setMyLocationEnabled(true);
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }

                try {
                    currentLoc = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLoc).build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18.0f));
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                if(startTracking){
                    // https://www.youtube.com/watch?v=rN7x3ovWepM
                    // Used this to learn how track location
                    Log.i(TAG, "onMapReady: Tracking is enabled");
                    mLocationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            // Draws the line between old point and new point
                            LatLng newLoc =  new LatLng(location.getLatitude(), location.getLongitude());
                            // https://www.youtube.com/watch?v=xl0GwkLNpNI
                            // Adding a route to the map
                            Polyline route = mGoogleMap.addPolyline(
                                    new PolylineOptions()
                                            .add(currentLoc)
                                            .add(newLoc));
                            route.setColor(R.color.colorPrimaryDark);
                            route.setClickable(false);

                            if(newLoc != currentLoc) {
                                distance += distance((float)currentLoc.latitude, (float)currentLoc.longitude,
                                        (float)newLoc.latitude, (float)newLoc.longitude);
                            }


                            currentLoc = newLoc;
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18.0f));



                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };

                    try{
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                                minDistance, mLocationListener);}
                    catch (SecurityException e){
                        Log.i(TAG, "onMapReady: failed to requestLocationUpdates()");
                        e.printStackTrace();
                    }

                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            seconds += 1;
                            try {
                                mListener.sendDistance(distance, seconds);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }, 0, 1000);
                }


            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
        mMapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MapFragmentListener){
            mListener = (MapFragmentListener) context;
        } else{
            throw new RuntimeException(context.toString() + " must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // https://stackoverflow.com/questions/8832071/how-can-i-get-the-distance-between-two-point-by-latlng
    public float distance (float lat_a, float lng_a, float lat_b, float lng_b )
    {
        LatLng startLatLng = new LatLng(lat_a, lng_a);
        LatLng endLatLng = new LatLng(lat_b, lng_b);
        double distance = SphericalUtil.computeDistanceBetween(startLatLng, endLatLng);
        return (float) (distance);
//        double earthRadius = 3958.75;
//        double latDiff = Math.toRadians(lat_b-lat_a);
//        double lngDiff = Math.toRadians(lng_b-lng_a);
//        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
//                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
//                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        double distance = earthRadius * c;
//
//        int meterConversion = 1609;

//        return new Float(distance * meterConversion).floatValue();
    }
}
