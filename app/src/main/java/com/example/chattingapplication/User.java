package com.example.chattingapplication;

import android.net.Uri;

public class User {
    public String name,phnno,uid,download_url;
    User(){}
    User(String name,String phnno,String uid,String download_url)
    {
        this.name = name;
        this.phnno = phnno;
        this.uid = uid;
        this.download_url = download_url;
    }
}
