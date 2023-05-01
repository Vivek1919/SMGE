package com.example.smge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.smge.Admin.Chat_to_user_adapter;
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
import com.google.firebase.database.annotations.Nullable;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private TextView sendTo;
    private Button sendButton;
    private RecyclerView chatRecyclerView;
    private Chat_to_user_adapter chatAdapter;
    private List<Chat> chatList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String receiver;
    private DatabaseReference chatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendTo = findViewById(R.id.sendTo);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        // Pass the current user's email to the adapter
        chatAdapter = new Chat_to_user_adapter(chatList, firebaseAuth.getCurrentUser().getEmail());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        // Get the receiver ID
        receiver = getIntent().getStringExtra("receiverEmail");

        sendTo.setText(receiver);

        // Set the reference to the chats node
        // Query Realtime Database for all chats between the user and the admin
        chatsRef = firebaseDatabase.getReference("chats");
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSenderEmail().equals(receiver) || chat.getReceiverEmail().equals(receiver)) {
                        // Only add the message to the list if it's sent by the signed-in user or the admin
                        chatList.add(chat);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();

                if (!TextUtils.isEmpty(message)) {
                    // Store in Realtime Database
                    String key = chatsRef.push().getKey();
                    Chat chat = new Chat(message, UUID.randomUUID().toString(),"admin@gmail.com", receiver, new Date().getTime());
                    chatsRef.child(key).setValue(chat);
                    Log.d("ChatActivity", "Chat sent: " + chat);

                    // Add the new chat message to the list and notify the adapter of the change
                    chatList.add(chat);
                    chatAdapter.notifyItemInserted(chatList.size() - 1);

                    messageEditText.setText("");
                    chatRecyclerView.scrollToPosition(chatList.size() - 1);
                }
            }
        });

    }
}
