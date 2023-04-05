package com.example.smge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smge.Chat.Chat;
import com.example.smge.Chat.ChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private String receiver;
    private DatabaseReference chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // Get the receiver ID
        receiver = getIntent().getStringExtra("receiverEmail");

        // Set the reference to the chats node
        chatsRef = firebaseDatabase.getReference("chats");

        // Query Realtime Database for all chats between the user and the admin
        chatsRef.orderByChild("sender_receiver")
                .startAt(currentUser.getUid() + "_" + receiver)
                .endAt(receiver + "_" + currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Chat chat = snapshot.getValue(Chat.class);
                        chatList.add(chat);
                        chatAdapter.notifyDataSetChanged();
                        chatRecyclerView.scrollToPosition(chatList.size() - 1);
                        Log.d("Chatwithanengineer", "Chat added: " + chat.toString());
                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // Do nothing
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        // Do nothing
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        // Do nothing
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Chatwithanengineer", "Listen failed.", error.toException());
                    }
                });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();

                if (!TextUtils.isEmpty(message)) {
                    // Store in Realtime Database
                    String key = chatsRef.push().getKey();
                    Chat chat = new Chat(message, currentUser.getUid(), currentUser.getEmail(), receiver, new Date().getTime());
                    chatsRef.child(key).setValue(chat);
                    Log.d("ChatActivity", "Chat sent: " + chat);

                    messageEditText.setText("");
                }
            }
        });

        chatAdapter = new ChatAdapter(chatList, receiver);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

    }
}

