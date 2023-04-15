package com.example.chattingapplication;

import android.net.Uri;

public class User {
    public String name,email,uid,download_url;
    User(){}
    User(String name,String email,String uid,String download_url)
    {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.download_url = download_url;
    }
}
