package com.seffyo.kandaapptesting.activities.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seffyo.kandaapptesting.R;
import com.seffyo.kandaapptesting.common.Alert;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_COARSE_LOCATION = 999;
    private Alert alert;
    private ListView mDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView statusView;
    private static final String TAG = "BLUETOOTH_ACT";
    private List<BluetoothDevice> DeviceList;
    private int CountDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        init();
        alert = new Alert();
        mDevices = (ListView) findViewById(R.id.myDevicesInner);
        statusView = (TextView) findViewById(R.id.bluetooth_status);
        DeviceList = new List<BluetoothDevice>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<BluetoothDevice> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(BluetoothDevice bluetoothDevice) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends BluetoothDevice> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends BluetoothDevice> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public BluetoothDevice get(int i) {
                return null;
            }

            @Override
            public BluetoothDevice set(int i, BluetoothDevice bluetoothDevice) {
                return null;
            }

            @Override
            public void add(int i, BluetoothDevice bluetoothDevice) {

            }

            @Override
            public BluetoothDevice remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<BluetoothDevice> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<BluetoothDevice> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<BluetoothDevice> subList(int i, int i1) {
                return null;
            }
        };
        CountDevices = 0;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Setting Home to go one level up in UI instead of all the way to front page

        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter = ((BluetoothManager)this.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();

        if(!isBluetoothSupported(mBluetoothAdapter)) {
            alert.ShowAlert(this,"Error","This device does not support Bluetooth");
        }
        else
        {
            // Bluetooth is supported, if not enabled, do so
            if (!mBluetoothAdapter.isEnabled()) {
                showStatus("Enabling Bluetooth");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                showStatus("Bluetooth Enabled");
                int state = mBluetoothAdapter.getState();
                String mydeviceaddress = mBluetoothAdapter.getAddress();
                String mydevicename = mBluetoothAdapter.getName();
                statusView.append("Device Name: " + mydevicename + "\r\n");
                statusView.append("Device Address: " + mydeviceaddress + "\r\n");
                statusView.append("Device State: " + getLiteralState(state) + "\r\n");

            }
        }

    }

    // Returns true if permissions are set
    protected boolean checkLocationPermission() {
        showStatus("Checking RUNTIME");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ProceedDiscovery(); // --->
                } else {
                    showStatus("ERROR RUNTIME");
                }
                break;
            }
        }
    }

    void ProceedDiscovery(){
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                showStatus("Discovery Started");
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                statusView.append("Found device: " + deviceHardwareAddress + "\r\n");

                CountDevices++;
                addDeviceToView(deviceHardwareAddress, CountDevices);

                DeviceList.add(device);
                //showDeviceFound(deviceName, deviceHardwareAddress);
            }
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                showStatus("Discovery Finished");
            }
        }
    };
    public void showDeviceFound(String deviceName, String deviceHardwareAddress){
        alert.ShowAlert(this,"Device!","Found device:" + deviceHardwareAddress);
    }

    // TODO: THIS DOES NOT WORK!
    public void ConnectToDevice(View view){
        Button btn = (Button) view;
        int deviceIndex = Integer.parseInt(btn.getTag().toString());
        ConnectThread connectionToDevice = new ConnectThread(DeviceList.get(deviceIndex));
    }

    void addDeviceToView(String deviceHardwareAddress, int countDevices){
        Button btnName = new Button(this);
        btnName.setTag(countDevices);
        btnName.setText("#"+countDevices+" Name: xxxx \r\n MAC: " + deviceHardwareAddress);
        btnName.setLayoutParams(new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT
        ));
        btnName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ConnectToDevice(v);
            }
        });
        //mDevices.addView(btnName);
    }

    String getLiteralState(int status){
        switch (status){
            case 10:{
                return "STATE_OFF";
            }
            case 11:{
                return  "STATE_TURNING_ON";
            }
            case 12:{
                return "STATE_ON";
            }
            case 13:{
                return "STATE_TURNING_OFF";
            }
            default:
                return "UNKNOWN STATE";
        }
    }

    public void showStatus(String status){
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
        //alert.ShowAlert(this,"Status", status);
    }

    public void discoverDevices(View view){
        if(!checkLocationPermission()){
            ProceedDiscovery();
        }

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        if(mBluetoothAdapter.startDiscovery()){
            // Toast.makeText(this, "Starting discovery", Toast.LENGTH_LONG).show();
        }
        else
            alert.ShowAlert(this,"Discovery failed", "Did not start!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    void init(){
        //addFloatingActionButton();
        addToolBar();
    }

    /*
    void addFloatingActionButton(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    */

    void addToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    boolean isBluetoothSupported(BluetoothAdapter mBluetoothAdapter){
        return mBluetoothAdapter != null;
    }

    public void displayPairedDevices(View view){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            mDevices.removeAllViews();
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Button btnName = new Button(this);
                btnName.setText("Name: " + deviceName + " \r\n MAC: " + deviceHardwareAddress);
                btnName.setLayoutParams(new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT
                        ));
                mDevices.addView(btnName);
            }
        }
        else{
            alert.ShowAlert(this,"No devices connected","No Bluetooth devices connected!");
        }
    }

    void manageMyConnectedSocket(BluetoothSocket mmSocket){
        showStatus("CONNECTED");
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }

    }
}
