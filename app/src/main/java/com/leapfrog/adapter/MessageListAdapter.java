package com.leapfrog.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.Message;
import com.leapfrog.model.User;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.SettingsPreference;
import com.leapfrogandroid.R;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/**
 * Holds the components required to allow the user to view messages
 */
public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    public List<Message> messageList;

    private User current, other;

    private SharedPreferences preferences;
    /**
     * Set private message component variables
     */
    public MessageListAdapter(Context context, List<Message> messageList, User current, User other){
        this.context = context;
        this.messageList = messageList;

        this.current = current;
        this.other = other;

        refresh();

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get 30 most recent messages
     */
    public void refresh() {
        messageList.clear();
        messageList.addAll(LeaperDatabase.getInstance(context).getMessages(current, other));
        notifyDataSetChanged();
    }
    /**
     * Get Messages
     */
    public void loadPreviousMessages() {

        notifyDataSetChanged();
    }
    /**
     * Add message to list
     */
    public synchronized void appendMessage(Message message){
        messageList.add(0, message);
        LeaperDatabase.getInstance(context).insertMessageData(message);
        notifyItemInserted(0);
    }
    /**
     * Add message to list
     */
    public void sendMessage(final Message message){
        appendMessage(message);
        notifyDataSetChanged();
    }
    /**
     * Get message class
     */
    @Override
    public int getItemViewType(int position){
        Message message = messageList.get(position);

        if(message.getSender().getUserID().equals(Authentication.getUsername(context))){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }
    /**
     * Create layout
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new View(context);

        if(viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        }else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return new SentMessageHolder(view);
    }
    /**
     * Maintain layout
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    /**
     * Get number of messages
     */
    public int getItemCount() {
        return messageList.size();
    }
    /**
     * Holds all the components required to allow the user to view received messages
     */
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);

            messageText.setTextColor(SettingsPreference.getColor(context));
        }
        /**
         * Update and set message components
         */
        void bind(Message message){
            messageText.setText(message.getMessage());
            nameText.setText(message.getSender().getNickname());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(message.getCreatedAt());
            String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            timeText.setText(time);
        }
    }
    /**
     * Holds all the components required to allow the user to view sent message
     */
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);

            messageText.setTextColor(SettingsPreference.getColor(context));
        }
        /**
         * Update and set message components
         */
        void bind(Message message){
            messageText.setText(message.getMessage());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(message.getCreatedAt());
            String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            timeText.setText(time);
        }
    }
}
