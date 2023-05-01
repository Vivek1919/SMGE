package com.example.smge.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smge.Chat.Chat;
import com.example.smge.ChatActivity;
import com.example.smge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminChatView extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatsRef;
    private List<String> senderChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView1);
        senderChats = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(senderChats);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatListAdapter);


        // Set the reference to the chats node
        chatsRef = firebaseDatabase.getReference("chats");
        Query query = chatsRef.orderByChild("senderEmail");

        // Query Realtime Database for all chats between the user and the admin
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> senders = new HashSet<>();
                chatList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    String senderEmail = chat.getSenderEmail();
                    senders.add(senderEmail);
                    chatList.add(chat);
                }
                // Do something with uniqueSenderEmails set
                senderChats = new ArrayList<>();
                senderChats.addAll(senders);
                senderChats.remove("admin@gmail.com");
                chatListAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });

        chatListAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get the selected chat item
                String selectedChat = senderChats.get(position);

                // Start the ChatActivity for the selected sender
                Intent intent = new Intent(AdminChatView.this, ChatActivity.class);
                intent.putExtra("receiverEmail", selectedChat);
                startActivity(intent);
            }
        });
    }
}

