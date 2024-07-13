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
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        final String senderId=auth.getUid();
        String receiverId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");

        binding.userNameChat.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        final ArrayList<MessageModel>messageModels=new ArrayList<>();
        final ChattingAdapter chattingAdapter=new ChattingAdapter(messageModels,this);
        binding.RecycleViewChat.setAdapter(chattingAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.RecycleViewChat.setLayoutManager(layoutManager);

        // message send A to B then id(sender Room)=A+B
        //(receiver Room)=B+A   (b is Receiver and A is sender)
        final String senderRoom= senderId + receiverId;
        final String receiverRoom= receiverId + senderId;

        // we get data from firebase to recycle view in chat Activity
        // we use senderRoom because use login karega tab vo sender hi h
        //see this part in Realtime database part in AndroidStudio of Firebase(go to Tools then Firebase then Realtime then read data from firebase)

        database.getReference().child("Chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //snapshot to get data
                                        //but so many user so loop in snapshot(for each)
                                        messageModels.clear();  // to show data only one time(not show sended chat again and again)
                                        for(DataSnapshot snapshot1:snapshot.getChildren())
                                        {
                                            MessageModel model=snapshot1.getValue(MessageModel.class);
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
                String message=binding.etMessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                //Empty editText when send button is Clicked
                binding.etMessage.setText(" ");

                // creating child as chats in firebase
                // .push() for creating id of each message
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //sender wala kaam to ho gaya ab receiver wala bhi kar denge success hone par
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