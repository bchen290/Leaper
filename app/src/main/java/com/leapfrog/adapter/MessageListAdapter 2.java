package com.leapfrog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.model.Message;
import com.leapfrog.model.User;
import com.leapfrog.util.Authentication;
import com.leapfrogandroid.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context context;
    public List<Message> messageList;

    private User current, other;

    public MessageListAdapter(Context context, List<Message> messageList, User current, User other){
        this.context = context;
        this.messageList = messageList;

        this.current = current;
        this.other = other;

        refresh();
    }

    // Loads 30 most recent messages
    private void refresh() {
        messageList.addAll(LeaperDatabase.getInstance(context).getMessages(current, other));
        notifyDataSetChanged();
    }

    public void loadPreviousMessages() {
        //final long lastTimestamp = messageList.get(messageList.size() - 1).getCreatedAt();
        //TODO actually get messages
        notifyDataSetChanged();
    }

    public synchronized void appendMessage(Message message){
        messageList.add(0, message);
        LeaperDatabase.getInstance(context).insertMessageData(message);
        notifyItemInserted(0);
    }

    public void sendMessage(final Message message){
        appendMessage(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){
        Message message = messageList.get(position);

        if(message.getSender().getUserID().equals(Authentication.getUsername(context))){
            return VIEW_TYPE_MESSAGE_SENT;
        }else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

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
    public int getItemCount() {
        return messageList.size();
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message){
            messageText.setText(message.getMessage());
            nameText.setText(message.getSender().getNickname());
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message){
            messageText.setText(message.getMessage());
        }
    }
}
