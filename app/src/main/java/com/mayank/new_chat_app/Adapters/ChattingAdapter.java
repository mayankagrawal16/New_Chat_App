//package com.mayank.new_chat_app.Adapters;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//import com.mayank.new_chat_app.MessageModel;
//import com.mayank.new_chat_app.R;
//
//import java.util.ArrayList;
//
//public class ChattingAdapter extends RecyclerView.Adapter{
//
//    // Jo user login h vo sender h and login user ke liye (FireBaseAuth.getInstance().getUid())
//    ArrayList<MessageModel>messageModels;
//    String recId;
//    Context context;
//
//    int SENDER_VIEW_TYPE=1;
//    int RECEIVER_VIEW_TYPE=2;
//
//    public ChattingAdapter(ArrayList<MessageModel> messageModels, Context context) {
//        this.messageModels = messageModels;
//        this.context = context;
//    }
//
//    public ChattingAdapter(ArrayList<MessageModel> messageModels, Context context,String recId) {
//        this.messageModels = messageModels;
//        this.recId = recId;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if(viewType==SENDER_VIEW_TYPE)
//        {
//            //same work as we do
//            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
//            return new SenderViewHolder(view);
//        }
//        else {
//            View view=LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
//            return new ReceiverViewHolder(view);
//        }
//    }
//
//    // two viewHolder so we have to implement below method
//    @Override
//    public int getItemViewType(int position) {
//        // Help to know which view holder is choosen it means sender layout occur or receiver is deceided by
//        // this below condition(their is only 1 sender and multiple receiver on single device(can get mess from many users)).
//
//        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
//        {
//            // there id are equals it means it is sender sending message to other so use yellow layout
//            // Easy to identify sender with help of Firebase Auth user id
//            // condition check that id equals to id of logged in user or not
//            return SENDER_VIEW_TYPE;
//        }
//        else {
//            return RECEIVER_VIEW_TYPE;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        // isme data ko set karna hota h
//        //sender and receiver ke message ko identify karna h and sender ka message identify karna easy h
//        // Jo user login h vo sender
//        MessageModel messageModel=messageModels.get(position);
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Delete")
//                        .setMessage("Are you Sure You Want to delete the Message")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // deleting the message id
//                                FirebaseDatabase database=FirebaseDatabase.getInstance();
//                                // Going into the sender Room=senderId+receiverId
//                                String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
//                                database.getReference().child("Users")
//                                        .child(senderRoom)
//                                        .child(messageModel.getMessageId())
//                                        .setValue(null);
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//                return false;
//            }
//        });
//
//
//        if(holder.getClass()==SenderViewHolder.class)
//        {
//            // first data send and then save in firebase and get it in as received message
//            ((SenderViewHolder) holder).senderMess.setText(messageModel.getMessage());
//            //((SenderViewHolder) holder).senderTime.setText(messageModel.getTimestamp().toString());
//        }
//        else {
//            ((ReceiverViewHolder)holder).receiverMess.setText(messageModel.getMessage());
//            //((ReceiverViewHolder)holder).receiverTime.setText(messageModel.getTimestamp().toString());
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return messageModels.size();
//    }
//
//    // we have to create two view holder because we have receiver and sender(two layouts)
//    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
//        TextView receiverMess,receiverTime;
//        public ReceiverViewHolder(@NonNull View itemView) {
//            super(itemView);
//            receiverMess = itemView.findViewById(R.id.receiverText);
//            receiverTime=itemView.findViewById(R.id.receiverTime);
//        }
//    }
//    public class SenderViewHolder extends RecyclerView.ViewHolder {
//        TextView senderMess,senderTime;
//        public SenderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            senderMess=itemView.findViewById(R.id.senderText);
//            senderTime=itemView.findViewById(R.id.senderTime);
//        }
//    }
//}
package com.mayank.new_chat_app.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mayank.new_chat_app.MessageModel;
import com.mayank.new_chat_app.R;

import java.util.ArrayList;

public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Jo user login h vo sender h and login user ke liye (FireBaseAuth.getInstance().getUid())
    ArrayList<MessageModel> messageModels;
    String recId;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChattingAdapter(ArrayList<MessageModel> messageModels, Context context) {
          this.messageModels = messageModels;
          this.context = context;
      }
    public ChattingAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.recId = recId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            //same work as we do
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    // two viewHolder so we have to implement below method
    @Override
    public int getItemViewType(int position) {
        // Help to know which view holder is choosen it means sender layout occur or receiver is deceided by
        // this below condition(their is only 1 sender and multiple receiver on single device(can get mess from many users)).
        if (messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
            // there id are equals it means it is sender sending message to other so use yellow layout
            // Easy to identify sender with help of Firebase Auth user id
            // condition check that id equals to id of logged in user or not
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // isme data ko set karna hota h
        //sender and receiver ke message ko identify karna h and sender ka message identify karna easy h
        // Jo user login h vo sender
        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // deleting the message id
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // Going into the sender Room=senderId+receiverId
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats")
                                        .child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .removeValue();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return false;
            }
        });

        if (holder.getClass() == SenderViewHolder.class) {
            // first data send and then save in firebase and get it in as received message
            ((SenderViewHolder) holder).senderMess.setText(messageModel.getMessage());
            //((SenderViewHolder) holder).senderTime.setText(messageModel.getTimestamp().toString());
        } else {
            ((ReceiverViewHolder) holder).receiverMess.setText(messageModel.getMessage());
            //((ReceiverViewHolder) holder).receiverTime.setText(messageModel.getTimestamp().toString());
        }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    // we have to create two view holder because we have receiver and sender(two layouts)
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMess, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMess = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMess, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMess = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
