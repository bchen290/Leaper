package com.leapfrog.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leapfrog.MainActivity;
import com.leapfrog.adapter.MessageListAdapter;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.Message;
import com.leapfrogandroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {
    private MessageListAdapter mChatAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Button mSendButton;
    private EditText mMessageEditText;

    public BluetoothAdapter bluetoothAdapter;

    private volatile String message = "";
    public volatile Thread bluetoothClientThread, bluetoothServerThread, bluetoothServerControllerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_messages);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final String macID = getIntent().getStringExtra("ChatSession");

        mSendButton = findViewById(R.id.button_chatbox_send);
        mMessageEditText = findViewById(R.id.edittext_chatbox);

        mRecyclerView = findViewById(R.id.recyclerview_message_list);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mChatAdapter = new MessageListAdapter(this, new ArrayList<Message>(), macID, MainActivity.currentUser.getUserID());
        mRecyclerView.setAdapter(mChatAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mMessageEditText.getText().toString();

                if(!message.isEmpty()) {
                    mChatAdapter.sendMessage(message);

                    try {
                        bluetoothClientThread = new BluetoothClient(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macID));
                        bluetoothClientThread.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mMessageEditText.setText("");
                }
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                    mChatAdapter.loadPreviousMessages();
                }
            }
        });

        try {
            bluetoothServerControllerThread = new BluetoothServerController();
            bluetoothServerControllerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class BluetoothServerController extends Thread {
        private BluetoothServerSocket bluetoothServerSocket;

        BluetoothServerController () throws IOException {
            bluetoothServerSocket = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("LeapFrogChat", MainActivity.BLUETOOTH_UUID);
        }

        @Override
        public void run(){
            while(true){
                try {
                    BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();

                    if(bluetoothSocket != null){
                        bluetoothServerThread = new BluetoothServer(bluetoothSocket);
                        bluetoothServerThread.start();
                    }
                } catch (IOException e){
                    break;
                }
            }
        }
    }

    public class AddMessage extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_private_messages);


            final EditText msg = findViewById(R.id.edittext_chatbox);

            Button sendMessage = findViewById(R.id.button_chatbox_send);
            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LeaperDatabase.getInstance(ChatActivity.this).insertTable2Data(msg.getText().toString());
                }
            });
        }
    }

    class BluetoothServer extends Thread {
        private InputStream inputStream;
        private BluetoothSocket bluetoothSocket;

        BluetoothServer(BluetoothSocket socket) throws IOException {
            inputStream = socket.getInputStream();
            bluetoothSocket = socket;
        }

        @Override
        public void run() {
            try {
                String text = "";

                while (true) {
                    int next = -1;

                    try {
                        next = bluetoothSocket.getInputStream().read();
                    }catch (IOException e){
                        Log.e("TEST", "Could not read");
                    }

                    if (next == 0 || next == -1) {
                        break;
                    }

                    text += (char) next;
                }

                inputStream.close();
                bluetoothSocket.close();

                final String finalText = text;
                if (!text.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message temp = new Message();
                            temp.setCreatedAt(System.currentTimeMillis());
                            temp.setSender(MainActivity.otherUser);
                            temp.setMessage(finalText);
                            mChatAdapter.appendMessage(temp);
                        }
                    });
                }

                Log.e("TEST", mChatAdapter.messageList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class BluetoothClient extends Thread {
        private BluetoothSocket socket;

        BluetoothClient(BluetoothDevice device) throws IOException {
            socket = device.createInsecureRfcommSocketToServiceRecord(MainActivity.BLUETOOTH_UUID);
        }

        @Override
        public void run(){
            try {
                socket.connect();
                OutputStream outputStream = socket.getOutputStream();

                outputStream.write(message.getBytes());
                outputStream.write(0);
                outputStream.flush();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

