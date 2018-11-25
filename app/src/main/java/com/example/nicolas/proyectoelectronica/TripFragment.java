package com.example.nicolas.proyectoelectronica;


import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.location.Location;
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
import android.widget.Button;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import im.delight.android.location.SimpleLocation;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    View mView;
    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private TextView tv_km;
    ConnectedThread mConnectedThread;
    private FirebaseAuth firebaseAuth;
    private Button btn_start;
    private Button btn_end;
    private Boolean onTrip=true;


    ArrayList markerPoints = new ArrayList();


    public TripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mView = inflater.inflate(R.layout.fragment_trip, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map);
        tv_km = mView.findViewById(R.id.tv_km);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_end=mView.findViewById(R.id.btn_stop);
        btn_start=mView.findViewById(R.id.btn_start);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrip=false;
                Calendar calendar = Calendar.getInstance();
                Date currentTime = calendar.getTime();
                String user_id = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference(user_id).child("viajes").child("viaje de: "+currentTime);
                Map newPost = new HashMap();
                newPost.put("Kilometros",distance);
                newPost.put("Recorrido",markerPoints);
                current_user_db.setValue(newPost);
                distance=0;
                markerPoints.clear();
                tv_km.setText("KM: "+distance);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrip= true;
            }
        });

        if (mMapView != null) {
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
    double distance = 0;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("your_preferences", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String incomingMessagetmp = sharedPreferences.getString("incomingMessage", "DEFAULT");
                editor.clear().commit();
                String incomingMessage = incomingMessagetmp;
                Log.d(TAG, " TRIP FRAGMENT: "+incomingMessage);
                Log.d(TAG, " ON TRIP: "+onTrip);

                if (incomingMessage != "DEFAULT") {
                    try {

                        String[] mesg = incomingMessage.split(",");
                        String hora = mesg[0];
                        String lat = mesg[1];
                        String lon = mesg[2];


                        Log.d(TAG, "hora: " + hora);
                        Log.d(TAG, "lat: " + lat);
                        Log.d(TAG, "lon: " + lon);


                        LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                        if (onTrip==true){
                            markerPoints.add(point);
                            Polyline line = mMap.addPolyline(new PolylineOptions()
                                    .addAll(markerPoints)
                                    .width(5)
                                    .color(Color.RED));
                            Log.d(TAG, String.valueOf(markerPoints.size()));
                            if (markerPoints.size() >= 2) {
                                Log.d(TAG, "if");
                                LatLng a = (LatLng) markerPoints.get(markerPoints.size() - 1);
                                LatLng b = (LatLng) markerPoints.get(markerPoints.size() - 2);
                                Location locationA = new Location("point A");
                                locationA.setLatitude(a.latitude);
                                locationA.setLongitude(a.longitude);

                                Location locationB = new Location("point B");

                                locationB.setLatitude(b.latitude);
                                locationB.setLongitude(b.longitude);
                                Log.d(TAG, "dista: " + distance);
                                distance += (locationA.distanceTo(locationB)) / 1000;
                                tv_km.setText("KM: " + distance);
                            }
                        }
                        //float distance = locationA.distanceTo(locationB);
                    } catch (Exception e) {
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
        timerHandler.postDelayed(timerRunnable, 0);
        timerHandler2.postDelayed(timerRunnable2, 0);

    }

    //Stop task when the user quits the activity
    @Override
    public void onPause() {
        super.onPause();
        shouldRun = false;
        timerHandler.removeCallbacksAndMessages(timerRunnable);
        timerHandler2.removeCallbacksAndMessages(timerRunnable2);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = BluetoothSettings.SocketHandler.getSocket();
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    final String incomingMessage = new String(buffer, 0, bytes);

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("your_preferences", BluetoothSettings.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();

                            editor.putString("incomingMessage", incomingMessage);
                            editor.commit();

                        }
                    });


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage());
                    break;
                }
            }
        }
    }

    private Handler timerHandler2 = new Handler();

    int i;

    private Runnable timerRunnable2 = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                String user_id = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference(user_id).child("kilometros");
                //for (int i = 1; i <= 24; i++) {
                    timerHandler2.postDelayed(this, 10000);


                i+=1;
                Log.d(TAG, "before");
                //Map newPost = new HashMap();
                Map<String, Double> newPost = new HashMap();
                newPost.put("Kilometros "+i,distance);
                //                FirebaseDatabase.getInstance().getReference(user_id).child("kilometros").child("Kilometros "+i).setValue(distance);
                //current_user_db.push().setValue(newPost);
                Log.d(TAG, "after distance: "+ distance);

            }
        }


    };

}





