package com.example.nicolas.proyectoelectronica;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment implements OnMapReadyCallback{
    MapView mMapView;
    View mView;
    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    ArrayList markerPoints= new ArrayList();



    public TripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mView=inflater.inflate(R.layout.fragment_trip, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView= mView.findViewById(R.id.map);
        if(mMapView!=null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng villada = new LatLng(-31.362533, -64.276031);
        mMap.addMarker(new MarkerOptions().position(villada).title("ITS Villada"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(villada, 16));
        markerPoints.clear();

/*        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG,""+latLng);
                markerPoints.add(latLng);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .addAll(markerPoints)
                        .width(5)
                        .color(Color.RED));

            }
        });*/



    }

    private Handler timerHandler = new Handler();
    private boolean shouldRun = true;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                /* Put your code here */
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("your_preferences", getContext().MODE_PRIVATE);
                String incomingMessage = sharedPreferences.getString("incomingMessage","DEFAULT");
                if (incomingMessage!="DEFAULT"){
                    try {
                    String[] mesg = incomingMessage.split(",");
                    String lat = mesg[0];
                    String lon = mesg[1];
                    String vel = mesg[2];

                        LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        Log.d(TAG, "vel "+vel);
                        markerPoints.add(point);
                        Log.d(TAG, "BEFORE DIBUJO: "+point);
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .addAll(markerPoints)
                                .width(5)
                                .color(Color.RED));
                        Log.d(TAG, "AFTER DIBUJO: "+point);
                    }catch(Exception e){
                        Log.d(TAG, "not a double ");
                    }


                }


                //run again after 200 milliseconds (1/5 sec)
                timerHandler.postDelayed(this, 2000);
            }
        }
    };

    //In this example, the timer is started when the activity is loaded, but this need not to be the case
    @Override
    public void onResume() {
        super.onResume();
        /* ... */
        timerHandler.postDelayed(timerRunnable, 0);
    }

    //Stop task when the user quits the activity
    @Override
    public void onPause() {
        super.onPause();
        /* ... */
        shouldRun = false;
        timerHandler.removeCallbacksAndMessages(timerRunnable);
    }
}
