package com.example.nicolas.proyectoelectronica;


import android.bluetooth.BluetoothSocket;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment  {
    View mView;
    ConnectedThread mConnectedThread;



    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_month, container, false);
        GraphView graph_month = mView.findViewById(R.id.graph_month);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(5, 8),
                new DataPoint(10, 10),
                new DataPoint(15, 3),
                new DataPoint(20, 6),
                new DataPoint(25, 1),
                new DataPoint(30, 9),



        });
        graph_month.addSeries(series);

        graph_month.getViewport().setMinX(0);
        graph_month.getViewport().setMaxX(31);
        graph_month.getViewport().setMinY(1);
        graph_month.getViewport().setMaxY(10);

        graph_month.getViewport().setYAxisBoundsManual(true);
        graph_month.getViewport().setXAxisBoundsManual(true);




        return mView;
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
                Log.d(TAG, "MonthFragment: "+incomingMessage);

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
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("your_preferences", BluetoothSettings.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();

                            editor.putString("incomingMessage" ,incomingMessage);
                            editor.commit();

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
