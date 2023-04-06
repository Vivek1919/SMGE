package com.example.smge.Chat;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smge.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> chatList;
    private String currentUserEmail;
    private DatabaseReference mDatabase;

    public ChatAdapter(List<Chat> chatList, String currentUserEmail) {
        this.chatList = chatList;
        this.currentUserEmail = currentUserEmail;


        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                if (chat.getSenderEmail().equals(currentUserEmail) || chat.getSenderEmail().equals("admin@gmail.com")) {
                    // Only add the message to the list if it's sent by the signed-in user or the admin
                    chatList.add(chat);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                int index = getItemIndex(chat);
                if (index != -1) {
                    chatList.set(index, chat);
                    notifyItemChanged(index);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                int index = getItemIndex(chat);
                if (index != -1) {
                    chatList.remove(index);
                    notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Do nothing
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    private int getItemIndex(Chat chat) {
        for (int i = 0; i < chatList.size(); i++) {
            if (chatList.get(i).getKey().equals(chat.getKey())) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;
        private TextView senderTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            dateTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Chat chat) {
            messageTextView.setText(chat.getMessage());
            senderTextView.setText(chat.getSenderEmail());
            dateTextView.setText(formatDate(chat.getTimestamp()));

            // Set gravity and background color based on whether the message is sent by the current user or not
            int backgroundColor, gravity;
            if (chat.getSenderEmail().equals(chat.getSenderEmail())) {
                backgroundColor = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryLight);
                gravity = Gravity.END;
            } else {
                backgroundColor = Color.WHITE;
                gravity = Gravity.START;
            }
            itemView.setBackgroundColor(backgroundColor);
            ((LinearLayout)itemView).setGravity(gravity);
        }

        // Helper method to format the timestamp into a readable date and time string
        private String formatDate(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}



