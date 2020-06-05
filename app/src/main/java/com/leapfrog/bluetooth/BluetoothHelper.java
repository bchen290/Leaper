package com.leapfrog.bluetooth;

//File Name: BluetoothHelper.java
//Purpose: This file holds methods to help with bluetooth functionality
//Version: 1.0, Last Edit Date: 05/14/2020
//Author: Brian Chen, Greg Dolan
//Dependencies: ConversationsActivity.java, ChatActivity.java

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.leapfrog.activities.ConversationsActivity;
import com.leapfrog.activities.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;

/**
 * Helper class for bluetooth connection
 */
public class BluetoothHelper {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    private AlertDialog pairedDeviceDialog;

    private BluetoothSocket socket;
    private BluetoothDevice device;

    /**
     * Initialize bluetooth helper
     * @param context Current Android context
     * @param bluetoothAdapter The default bluetooth adapter
     */
    public BluetoothHelper(Context context, BluetoothAdapter bluetoothAdapter){
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    /**
     * Gets all the paired devices
     * @return List of paired devices
     */
    public List<BluetoothDevice> getPairedDevices(){
        return new ArrayList<>(bluetoothAdapter.getBondedDevices());
    }

    /**
     * Shows all the paired devices in a dialog
     */
    public void showPairedBluetoothDevices() {
        final ArrayList<BluetoothDevice> pairedDeviceList = new ArrayList<>();
        ArrayList<String> pairedDeviceName = new ArrayList<>();

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    pairedDeviceList.addAll(bondedDevices);
                }
            }
        }

        for(BluetoothDevice device : pairedDeviceList){
            pairedDeviceName.add(device.getName());
        }

        final ArrayAdapter<String> pairedDeviceAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
        pairedDeviceAdapter.addAll(pairedDeviceName);
        pairedDeviceAdapter.add("Not on screen");

        pairedDeviceDialog = new AlertDialog.Builder(context).setAdapter(pairedDeviceAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Objects.equals(pairedDeviceAdapter.getItem(which), "Not on screen")) {
                    Toast.makeText(context, "Please pair with computer", Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    dialog.dismiss();
                    device = pairedDeviceList.get(which);

                    ((ConversationsActivity) context).onFinishUserDialog(device.getAddress());

                    bluetoothAdapter.cancelDiscovery();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("ChatSession", device.getAddress());
                    context.startActivity(intent);
                }
            }
        }).show();
    }
}