package com.mayank.new_chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mayank.new_chat_app.Adapters.ChattingAdapter;
//import com.mayank.new_chat_app.Models.MessageModel;
import com.mayank.new_chat_app.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    // tint-->used in xml to change color of icon and use app: instead of android: in xml code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userNameChat.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChattingAdapter chattingAdapter = new ChattingAdapter(messageModels, this, receiverId);
        binding.RecycleViewChat.setAdapter(chattingAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.RecycleViewChat.setLayoutManager(layoutManager);

        // Message send A to B then id(sender Room) = A+B
        // (receiver Room) = B+A   (B is receiver and A is sender)
        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        // We get data from Firebase to RecyclerView in chat activity
        // We use senderRoom because when user logs in, they are the sender
        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Snapshot to get data
                        // Loop in snapshot for each user
                        messageModels.clear();  // To show data only one time (not show sent chat again and again)
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chattingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etMessage.getText().toString().isEmpty()) {
                    binding.etMessage.setError("Enter a message");
                    return;
                }
                String message = binding.etMessage.getText().toString();
                final MessageModel model = new MessageModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                // Empty editText when send button is clicked
                binding.etMessage.setText("");

                // Creating child as chats in Firebase
                // .push() for creating id of each message
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Sender work is done, now receiver work on success
                                database.getReference().child("Chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });

    }
}
