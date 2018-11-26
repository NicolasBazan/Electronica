package com.example.nicolas.proyectoelectronica;


import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment  {
    View mView;
    ConnectedThread mConnectedThread;
    TextView count0a4000;
    TextView count4000a6000;
    TextView count6000a8000;
    TextView count8000a10000;
    TextView count10000a12000;
    TextView count12000a14000;

    TextView countmenos0a4000;
    TextView countmenos4000a6000;
    TextView countmenos6000a8000;
    TextView countmenos8000a10000;
    TextView countmenos10000a12000;
    TextView countmenos12000a14000;
    ;



    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_month, container, false);
        count0a4000 =mView.findViewById(R.id.count0a4000);
        count4000a6000 =mView.findViewById(R.id.count4000a6000);
        count6000a8000 =mView.findViewById(R.id.count6000a8000);
        count8000a10000 =mView.findViewById(R.id.count8000a10000);
        count10000a12000 =mView.findViewById(R.id.count10000a12000);
        count12000a14000 =mView.findViewById(R.id.count12000a14000);

        countmenos0a4000 =mView.findViewById(R.id.countmenos0a4000);
        countmenos4000a6000 =mView.findViewById(R.id.countmenos4000a6000);
        countmenos6000a8000 =mView.findViewById(R.id.countmenos6000a8000);
        countmenos8000a10000 =mView.findViewById(R.id.countmenos8000a10000);
        countmenos10000a12000 =mView.findViewById(R.id.countmenos10000a12000);
        countmenos12000a14000 =mView.findViewById(R.id.countmenos12000a14000);
        String a = TripFragment.aceleracion.aceleracion;
        if (a!="") {
            Log.d(TAG, " ACELERACIONES MONTH" + a);
            String[] mesg = a.split(",");

            String m4000 = mesg[0];
            String m6000 = mesg[1];
            String m8000 = mesg[2];
            String m10000 = mesg[3];
            String m12000 = mesg[4];
            String m14000 = mesg[5];
            if (m4000.contains("-")){
                countmenos0a4000.setText(m4000);
            }else {
                count0a4000.setText(m4000);
            }
            if (m6000.contains("-")){
                countmenos4000a6000.setText(m6000);

            }else {
                count4000a6000.setText(m6000);

            }
            if (m8000.contains("-")){
                countmenos6000a8000.setText(m8000);
            }else {
                count6000a8000.setText(m8000);

            }
            if (m10000.contains("-")){
                countmenos8000a10000.setText(m10000);

            }else {
                count8000a10000.setText(m10000);

            }
            if (m12000.contains("-")){
                countmenos10000a12000.setText(m12000);

            }else {
                count10000a12000.setText(m12000);

            }
            if (m14000.contains("-")){
                countmenos12000a14000.setText(m14000);

            }else {
                count12000a14000.setText(m14000);
            }
        }

        return mView;
    }
    private Handler timerHandler = new Handler();
    private boolean shouldRun = true;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (shouldRun) {
                /* Put your code here */



                }
                //run again after 200 milliseconds (1/5 sec)
                timerHandler.postDelayed(this, 2000);
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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = BluetoothSettings.SocketHandler.getSocket() ;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
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



                        }
                    });


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }
    }

}
