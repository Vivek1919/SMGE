package com.example.smge.Admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smge.Chat.Chat;
import com.example.smge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private OnItemClickListener onItemClickListener;
    private DatabaseReference mDatabase;
    private List<String> uniqueSenderEmails;
    private List<Chat> chats;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ChatListAdapter(List<String> chatList) {
        this.uniqueSenderEmails = chatList;

        // Set the reference to the chats node
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        Query query = mDatabase.orderByChild("senderEmail");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> senders = new HashSet<>();
                chats = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    String senderEmail = chat.getSenderEmail();
                    senders.add(senderEmail);
                    chats.add(chat);
                }
                // Do something with uniqueSenderEmails set
                uniqueSenderEmails = new ArrayList<>();
                uniqueSenderEmails.addAll(senders);
                uniqueSenderEmails.remove("admin@gmail.com");
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new ChatViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String email = uniqueSenderEmails.get(position);
        holder.senderEmailTextView.setText(email);
        List<Chat> senderChats = chats.stream().filter(i -> i.getSenderEmail().equals(email)).collect(Collectors.toList());
        Chat mostRecentChat = Collections.max(senderChats, Comparator.comparing(Chat::getTimestamp));
        String mostRecentMessage = mostRecentChat.getMessage();
        holder.lastMessageTextView.setText(mostRecentMessage);
    }


    @Override
    public int getItemCount() {
        return uniqueSenderEmails.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView senderEmailTextView;
        public TextView lastMessageTextView;
        private OnItemClickListener onItemClickListener;

        public ChatViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            senderEmailTextView = itemView.findViewById(R.id.senderEmailTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);

        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        }
    }
}

