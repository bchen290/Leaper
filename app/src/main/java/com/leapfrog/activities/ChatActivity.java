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

                    bluetoothClientThread = new ConnectThread(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macID));
                    bluetoothClientThread.start();

                    mMessageEditText.setText("");
                }
                LeaperDatabase.getInstance(ChatActivity.this).insertTable2Data(message);
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

        bluetoothServerControllerThread = new AcceptThread();
        bluetoothServerControllerThread.start();
    }

    class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;

        AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("LeapFrogChat", MainActivity.BLUETOOTH_UUID);
            } catch (IOException e) {
                Log.e("ChatActivity", "Socket's listen() method failed", e);
            }

            mmServerSocket = tmp;
        }

        @Override
        public void run(){
            BluetoothSocket socket = null;

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

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("ChatActivity", "Could not close the connect socket", e);
            }
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

    class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MainActivity.BLUETOOTH_UUID);
            } catch (IOException e) {
                Log.e("ChatActivity", "Socket create method failed");
            }

            mmSocket = tmp;
        }

        @Override
        public void run(){
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();

                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = mmSocket.getOutputStream();

                            outputStream.write(message.getBytes());
                            outputStream.write(0);
                            outputStream.flush();
                            outputStream.close();
                            mmSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch(IOException closeException) {
                    Log.e("ChatActivity", "Could not close the client socket");
                }
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch(IOException closeException) {
                Log.e("ChatActivity", "Could not close the client socket");
            }
        }
    }
}

