package com.example.chattingapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Message> messages;
    int receive=1,sent =2;
    MessageAdapter(Context context,ArrayList<Message> messages)
    {
        this.context = context;
        this.messages = messages;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==1)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.recieve,parent,false);
            return new ReceiveViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sent,parent,false);
            return new SentViewHolder( view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass()==SentViewHolder.class){
            ((SentViewHolder) holder).sentmsg.setText(messages.get(position).message);
        }
        else
        {
            RecyclerView.ViewHolder viewHolder = (ReceiveViewHolder) holder;
            ((ReceiveViewHolder) holder).rmsg.setText(messages.get(position).message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid()==message.senderid) return sent;
        else return receive;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class SentViewHolder extends RecyclerView.ViewHolder
    {
        private TextView sentmsg;
        SentViewHolder(View view)
        {
            super(view);
            sentmsg = view.findViewById(R.id.text_sent);
        }
    }
    class ReceiveViewHolder extends RecyclerView.ViewHolder
    {
        private TextView rmsg;
        ReceiveViewHolder(View view)
        {
            super(view);
            rmsg = view.findViewById(R.id.text_receive);
        }
    }
}
