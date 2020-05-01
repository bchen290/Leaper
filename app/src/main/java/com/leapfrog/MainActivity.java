package com.leapfrog;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leapfrog.activities.ChatActivity;
import com.leapfrog.bluetooth.BluetoothHelper;
import com.leapfrog.database.AppDatabase;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.ChatSessions;
import com.leapfrog.model.User;
import com.leapfrogandroid.R;

import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity {
    public static final UUID BLUETOOTH_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    public static User currentUser;
    public static User otherUser;

    private ListView chatSessionListView;
    private List<ChatSessions> chatSessionList;
    private ArrayAdapter<ChatSessions> chatSessionListAdapter;

    private FloatingActionButton floatingActionButton;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHelper bluetoothHelper;

    LeaperDatabase leaperDatabase;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null){
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            }

            bluetoothHelper = new BluetoothHelper(this, bluetoothAdapter);
        }

        currentUser = new User("CurrentUser", "", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        otherUser = new User("OtherUser", "None", UUID.randomUUID().toString());

        chatSessionListView = findViewById(R.id.chat_sessions);
        chatSessionList = AppDatabase.getInstance(getApplicationContext()).chatSessionDao().getAll();

        chatSessionListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatSessionList);
        chatSessionListAdapter.setNotifyOnChange(true);

        chatSessionListView.setAdapter(chatSessionListAdapter);
        chatSessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("ChatSession", chatSessionList.get(position).chatID);
                startActivity(intent);
            }
        });

        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothHelper.showPairedBluetoothDevices();
            }
        });

        leaperDatabase = new LeaperDatabase(this);
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
            AppDatabase.getInstance(getApplicationContext()).messageDao().deleteAll();
            AppDatabase.getInstance(getApplicationContext()).chatSessionDao().deleteAll();

            refreshAdapterFromDatabase();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFinishUserDialog(String user) {
        ChatSessions chatSessions = new ChatSessions();
        chatSessions.setChatID(user);
        chatSessions.setNickname(user);
        AppDatabase.getInstance(getApplicationContext()).chatSessionDao().insertAll(chatSessions);

        refreshAdapterFromDatabase();
    }

    public void refreshAdapterFromDatabase(){
        chatSessionListAdapter.clear();
        chatSessionListAdapter.addAll(AppDatabase.getInstance(getApplicationContext()).chatSessionDao().getAll());
        chatSessionListAdapter.notifyDataSetChanged();
    }
}