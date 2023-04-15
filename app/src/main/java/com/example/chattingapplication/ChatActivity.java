package com.example.chattingapplication;

import static java.util.Objects.isNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chats;
    private EditText msg;
    private ImageView sendbutton;
    private MessageAdapter adapter;
    private Drawable drawable;
    private ArrayList<Message> messages = new ArrayList<>();
    private String receiverroom="",senderroom="";
    private DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_acitivity);
        chats = (RecyclerView) findViewById(R.id.chat_view);
        msg = (EditText) findViewById(R.id.msg_box);
        sendbutton = findViewById(R.id.send_button);
        adapter = new MessageAdapter(this,messages);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String receiver_uid = intent.getStringExtra("uid");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setIcon(getUrlDrawable(intent.getStringExtra("url")));
        chats.setAdapter(adapter);
        chats.setLayoutManager(new LinearLayoutManager(this));
        senderroom = receiver_uid + FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverroom =FirebaseAuth.getInstance().getCurrentUser().getUid()+receiver_uid;
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("chats").child(senderroom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = msg.getText().toString();
                Message message1 = new Message(message,FirebaseAuth.getInstance().getCurrentUser().getUid());
                dbref = FirebaseDatabase.getInstance().getReference();
                dbref.child("chats").child(senderroom).child("messages").push().setValue(message1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dbref.child("chats").child(receiverroom).child("messages").push().setValue(message1);
                    }
                });
                msg.setText("");
            }
        });
    }
    @SuppressWarnings("deprecation")
    public static Drawable getUrlDrawable(String url) {
        try {
            URL aryURI = new URL(url);
            URLConnection conn = aryURI.openConnection();
            InputStream is = conn.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(is);
            return new BitmapDrawable(bmp);
        } catch (Exception e) {
            return null;
        }
    }
}