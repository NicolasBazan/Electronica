package com.example.nicolas.proyectoelectronica;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import im.delight.android.location.SimpleLocation;

import static android.content.ContentValues.TAG;


public class BluetoothSettings extends AppCompatActivity {

    private Toolbar toolbar;
    private Button showDevices;
    private ListView listDevices;
    public DeviceListAdapter mDeviceListAdapter;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    private static final String TAG = "BluetoothSettings";
    private int REQUEST_ENABLE_BT=1;
    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    ConnectedThread mConnectedThread;
    String incomingMessage="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_settings);
        toolbar = findViewById(R.id.customToolbar);
        listDevices = findViewById(R.id.listDevices);

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //first cancel discovery because its very memory intensive.
                mBluetoothAdapter.cancelDiscovery();

                Log.d(TAG, "onItemClick: You Clicked on a device.");
                String deviceName = mBTDevices.get(i).getName();
                String deviceAddress = mBTDevices.get(i).getAddress();
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
                ConnectThread connectThread = new ConnectThread(mBTDevices.get(i));
                Log.d(TAG, "connectThread");
                connectThread.start();
            }
        });
        showDevices = findViewById(R.id.showDevices);
        toolbar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            startActivity(new android.content.Intent(BluetoothSettings.this, MainActivity.class));
            }
        });
        setSupportActionBar(toolbar);
        showDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices

                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    mBTDevices.add(device);
                    Log.d(TAG, "" + device);

                }
                mDeviceListAdapter = new DeviceListAdapter(BluetoothSettings.this, R.layout.device_adapter_view, mBTDevices);
                listDevices.setAdapter(mDeviceListAdapter);

                }

        });

    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            // MY_UUID is the app's UUID string, also used by the server code
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "connectThread RUN");

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.d(TAG, "CONECTO: ");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Log.d(TAG, "connectException: ");
                Log.d(TAG, String.valueOf(connectException));

                try {
                    mmSocket.close();
                    Log.d(TAG, "closed");

                } catch (IOException closeException) { }
                return;
            }
            connected(mmSocket);


            // Do work to manage the connection (in a separate thread)
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
    class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        SimpleLocation location = new SimpleLocation(BluetoothSettings.this); //or context instead of this
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String latlong = latitude+","+longitude;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
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
            byte[] buffer = new byte[1];  // buffer store for the stream

            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            String trama= "";
            while (true) {
                // Read from the InputStream
                try {
                    bytes = mmInStream.read(buffer);
                    String tmp= new String(buffer, 0, bytes);
                    if (tmp.equals("$")){
                        trama="";
                    }else {
                        trama+=tmp;
                        if (trama.length()==38){
                            incomingMessage=trama;
                            trama="";

                        }else{
                            Log.d(TAG,"NO TIENE 38");
                        }
                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences = getSharedPreferences("your_preferences", BluetoothSettings.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("incomingMessage" , incomingMessage);
                            editor.commit();
                            Log.d(TAG,"incomiingMessage: "+ incomingMessage);




                        }
                    });


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                String text = new String(bytes, Charset.defaultCharset());
                Log.d(TAG, "Writing to outputStream: "+text);

                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG, "ERROR Writing to outputStream: "+e.getMessage());

            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    private void connected(BluetoothSocket mmSocket) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }
    private void share(String incomingMessage) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("incomingMessage", incomingMessage);
        startActivity(intent);
    }

    public static class SocketHandler {
        private static BluetoothSocket mmSocket;

        public static synchronized BluetoothSocket getSocket(){
            return mmSocket;
        }

        public static synchronized void setSocket(BluetoothSocket socket){
            SocketHandler.mmSocket = socket;
        }
    }

}
