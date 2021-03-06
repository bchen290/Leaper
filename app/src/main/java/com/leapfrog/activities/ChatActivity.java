package com.leapfrog.activities;

//File Name: ChatActivity.java
//Purpose: This file handles all the messaging
//Version: 1.0, Last Edit Date: 05/29/2020
//Author: Brian Chen
//Dependencies: AndroidManifest.xml, activity_chat.xml, MessageListAdapter.java, Message.java, User.java, Authentication.java,
//              BaseActivity.java, InternetConnectivity.java


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.leapfrog.adapter.MessageListAdapter;
import com.leapfrog.model.Message;
import com.leapfrog.model.User;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.BaseActivity;
import com.leapfrog.util.InternetConnectivity;
import com.leapfrogandroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This class holds all the necessary components to allow the user to message over bluetooth or data
 */
@SuppressWarnings("FieldCanBeLocal")
public class ChatActivity extends BaseActivity {
    public static final UUID BLUETOOTH_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private MessageListAdapter mChatAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Button mSendButton;
    private EditText mMessageEditText;

    public BluetoothAdapter bluetoothAdapter;

    private volatile String messageString = "";
    public volatile Thread bluetoothClientThread, bluetoothServerThread, bluetoothServerControllerThread;

    private User currentUser, otherUser;

    /**
     * Sets up chat activity
     * Initialize the two user objects, all the layout items, and the bluetooth threads
     * Create handler that refreshes activity every 10 seconds
     * Set on click listener to send message on send button clicked
     * @param savedInstanceState Information saved about current activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String chatID = getIntent().getStringExtra("ChatSession");
        setupToolbar(chatID);

        currentUser = new User(Authentication.getUsername(ChatActivity.this), "", Authentication.getUsername(ChatActivity.this));
        otherUser = new User(chatID, "", chatID);

        mSendButton = findViewById(R.id.button_chatbox_send);
        mMessageEditText = findViewById(R.id.edittext_chatbox);

        mRecyclerView = findViewById(R.id.recyclerview_message_list);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mChatAdapter = new MessageListAdapter(this, new ArrayList<>(), currentUser, otherUser);
        mRecyclerView.setAdapter(mChatAdapter);

        Handler handler = new Handler();
        Runnable handlerTask = new Runnable() {
            @Override
            public void run() {
                mChatAdapter.refresh();
                handler.postDelayed(this, 10000);
            }
        };

        handlerTask.run();

        mSendButton.setOnClickListener(v -> {
            messageString = mMessageEditText.getText().toString();

            if(!messageString.isEmpty()) {
                Message message = new Message();
                message.setCreatedAt(System.currentTimeMillis());
                message.setMessage(messageString);
                message.setSender(currentUser);
                message.setReceiver(otherUser);

                mChatAdapter.sendMessage(message);

                if (!InternetConnectivity.checkCachedInternet(this)) {
                    bluetoothClientThread = new ConnectThread(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(chatID));
                    bluetoothClientThread.start();
                }

                mMessageEditText.setText("");
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

        if (!InternetConnectivity.checkCachedInternet(this)) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            bluetoothServerControllerThread = new AcceptThread();
            bluetoothServerControllerThread.start();
        }
    }

    /**
     * Accepts bluetooth connection
     */
    class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;

        /**
         * Created a server socket to listen to bluetooth connection at UUID
         */
        AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("LeapFrogChat", BLUETOOTH_UUID);
            } catch (IOException e) {
                Log.e("ChatActivity", "Socket's listen() method failed", e);
            }

            mmServerSocket = tmp;
        }

        /**
         * This will wait until we receive a connection which will start another thread
         */
        @Override
        public void run(){
            BluetoothSocket socket;

            while(true){
                try {
                    socket = mmServerSocket.accept();

                    if (socket != null) {
                        bluetoothServerThread = new BluetoothServer(socket);
                        bluetoothServerThread.start();
                        mmServerSocket.close();
                        break;
                    }
                } catch (IOException e){
                    break;
                }
            }
        }

        /**
         * Cancels the server socket
         */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("ChatActivity", "Could not close the connect socket", e);
            }
        }
    }

    /**
     * Receives data from an established connection
     */
    class BluetoothServer extends Thread {
        private InputStream inputStream;
        private BluetoothSocket bluetoothSocket;

        /**
         * Sets up the data connection
         * @param socket The bluetooth socket to connect to
         * @throws IOException Exception if socket cannot be reached
         */
        BluetoothServer(BluetoothSocket socket) throws IOException {
            inputStream = socket.getInputStream();
            bluetoothSocket = socket;
        }

        /**
         * Read input until there's nothing to read then append this to the chat
         */
        @Override
        public void run() {
            try {
                StringBuilder text = new StringBuilder();

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

                    text.append((char) next);
                }

                inputStream.close();
                bluetoothSocket.close();

                final String finalText = text.toString();
                if (text.length() > 0) {
                    runOnUiThread(() -> {
                        Message temp = new Message();
                        temp.setCreatedAt(System.currentTimeMillis());
                        temp.setReceiver(currentUser);
                        temp.setSender(otherUser);
                        temp.setMessage(finalText);
                        mChatAdapter.appendMessage(temp);
                    });
                }

                Log.e("TEST", mChatAdapter.messageList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connects to a bluetooth server
     */
    class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        /**
         * Creates a client side connection to a server
         * @param device The device to connect to
         */
        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
            } catch (IOException e) {
                Log.e("ChatActivity", "Socket create method failed");
            }

            mmSocket = tmp;
        }

        /**
         * Writes data to the output stream
         */
        @Override
        public void run(){
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();

                try {
                    OutputStream outputStream = mmSocket.getOutputStream();

                    outputStream.write(messageString.getBytes());
                    outputStream.write(0);
                    outputStream.flush();
                    outputStream.close();
                    mmSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch(IOException closeException) {
                    Log.e("ChatActivity", "Could not close the client socket");
                }
            }
        }

        /**
         * Closes connection to server
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch(IOException closeException) {
                Log.e("ChatActivity", "Could not close the client socket");
            }
        }
    }
}

