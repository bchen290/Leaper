package com.leapfrog.activities;

//File Name: ConversationActivity.java
//Purpose: This one that holds all the different conversations
//Version: 1.0, Last Edit Date: 04/17/2020
//Author: Brian Chen, Greg Dolan, Omri Chashper
//Dependencies: AndroidManifest.xml, activity_conversations.xml, LeaperDatabase.java, BluetoothHelper.java, ChatSessions.java,
//              User.java, Authentication.java, BaseActivity.java, InternetConnectivity.java, Utils.java

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leapfrog.bluetooth.BluetoothHelper;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.ChatSessions;
import com.leapfrog.model.User;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.BaseActivity;
import com.leapfrog.util.InternetConnectivity;
import com.leapfrog.util.Utils;
import com.leapfrogandroid.R;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AlertDialog;

/**
 * Sets up the layout for conversation screen
 */
@SuppressWarnings("FieldCanBeLocal")
public class ConversationsActivity extends BaseActivity {
    private ListView chatSessionListView;
    private List<ChatSessions> chatSessionList;
    private ArrayAdapter<ChatSessions> chatSessionListAdapter;

    private FloatingActionButton floatingActionButton;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHelper bluetoothHelper;

    /**
     * @param savedInstanceState
     * Checks if there is internet connectivity, bluetooth, and authenticates
     */
    @SuppressLint("HardwareIds")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        setupToolbarNoUp("Leaper");

        if (Utils.hasNetworkAvailable(this)) {
            InternetConnectivity.cacheInternetState(this, true);
        } else {
            InternetConnectivity.cacheInternetState(this, false);

            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Not compatible")
                        .setMessage("Your phone does not support Bluetooth")
                        .setPositiveButton("Exit", (dialog, which) -> System.exit(0))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
                }

                bluetoothHelper = new BluetoothHelper(this, bluetoothAdapter);
            }
        }

        if (!Authentication.isAuthenticated(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        chatSessionListView = findViewById(R.id.chat_sessions);

        Set<ChatSessions> chatSessionsSet = new HashSet<>(LeaperDatabase.getInstance(this).getChatSessions(new User(Authentication.getUsername(this), "", Authentication.getUsername(this))));
        chatSessionList = new ArrayList<>(chatSessionsSet);

        chatSessionListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatSessionList);
        chatSessionListAdapter.setNotifyOnChange(true);

        chatSessionListView.setAdapter(chatSessionListAdapter);
        chatSessionListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ConversationsActivity.this, ChatActivity.class);
            intent.putExtra("ChatSession", chatSessionList.get(position).chatID);
            startActivity(intent);
        });

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(v -> {
            if (!InternetConnectivity.checkCachedInternet(this)) {
                bluetoothHelper.showPairedBluetoothDevices();
            } else {
                new LovelyTextInputDialog(this)
                        .setTitle("Enter username")
                        .setConfirmButton("Ok", text -> {
                            Intent intent = new Intent(ConversationsActivity.this, ChatActivity.class);
                            intent.putExtra("ChatSession", text);
                            startActivity(intent);
                        })
                        .show();
            }
        });
    }

    /**
     * @param menu Gets menu
     * @return returns true if method works
     * Creates the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item item in option menu
     * @return returns true when methods successfully works
     * Functionality of option menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show();

            // Delete the existing data
            LeaperDatabase.getInstance(this).deleteAll();

            refreshAdapterFromDatabase();

            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);

            return true;
        } else if(id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.logout) {
            Authentication.unauthenticate(this);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param user gets user
     * cleans up when user sends message and sets everything
     */
    public void onFinishUserDialog(String user) {
        ChatSessions chatSessions = new ChatSessions();
        chatSessions.setChatID(user);
        chatSessions.setNickname(user);
        refreshAdapterFromDatabase();
    }

    /**
     * Refreshes he adapter
     */
    public void refreshAdapterFromDatabase(){
        chatSessionListAdapter.clear();
        chatSessionListAdapter.notifyDataSetChanged();
    }
}