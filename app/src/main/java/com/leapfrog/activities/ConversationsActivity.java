package com.leapfrog.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leapfrog.bluetooth.BluetoothHelper;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.ChatSessions;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.BaseActivity;
import com.leapfrog.util.InternetConnectivity;
import com.leapfrog.util.Utils;
import com.leapfrogandroid.R;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

@SuppressWarnings("FieldCanBeLocal")
public class ConversationsActivity extends BaseActivity {
    private ListView chatSessionListView;
    private List<ChatSessions> chatSessionList;
    private ArrayAdapter<ChatSessions> chatSessionListAdapter;

    private FloatingActionButton floatingActionButton;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHelper bluetoothHelper;

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

        chatSessionList = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public void onFinishUserDialog(String user) {
        ChatSessions chatSessions = new ChatSessions();
        chatSessions.setChatID(user);
        chatSessions.setNickname(user);
        refreshAdapterFromDatabase();
    }

    public void refreshAdapterFromDatabase(){
        chatSessionListAdapter.clear();
        chatSessionListAdapter.notifyDataSetChanged();
    }
}