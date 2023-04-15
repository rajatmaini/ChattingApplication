package com.example.chattingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView userslist;
    private ArrayList<User> users;
    private UserAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference dbref;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userslist = (RecyclerView) findViewById(R.id.users_list);
        users = new ArrayList<>();
        adapter = new UserAdapter(this, users);
        userslist.setAdapter(adapter);
        userslist.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        dbref = FirebaseDatabase.getInstance().getReference();
        dbref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    User user = snap.getValue(User.class);
                    if (!user.uid.equals(mAuth.getUid())) users.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (item.getItemId()==R.id.logout)
            {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,Login.class);
                finish();
                startActivity(intent);
                return true;
            }
        return true;
    }
}