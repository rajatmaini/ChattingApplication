package com.example.chattingapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    Button signup,send_otp;
    EditText name,getPhn,otp;
    String ImagePath,download_url="";
    ImageView display_picture;
    DatabaseReference dbref;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageReference = FirebaseStorage.getInstance().getReference();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()== Activity.RESULT_OK)
            {
                Intent data = result.getData();
                Uri uri = data.getData();
                ImagePath = getPath(getApplicationContext(),uri);
                Log.d("path", ImagePath);
                display_picture.setImageURI(uri);
            }
        });
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        signup = findViewById(R.id.SignUp);
        name = (EditText) findViewById(R.id.Name);
        getPhn = (EditText) findViewById(R.id.PhoneNumber);
        otp = (EditText) findViewById(R.id.OTP);
        send_otp = findViewById(R.id.send_otp);
        display_picture = (ImageView) findViewById(R.id.display_picture);
        display_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        send_otp.setOnClickListener(view -> {
            String phnno = "+91"+getPhn.getText().toString();
            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                    SignUpFunction(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {

                }
            };
            PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phnno).setTimeout(60L, TimeUnit.SECONDS).setCallbacks(mCallbacks).build();
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            mAuth.useAppLanguage();

        });

//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String NAME = name.getText().toString();
//                String EMAIL = email.getText().toString();
//                String PASS = pass.getText().toString();
//                String CNFPASS = cnfpass.getText().toString();
//                if (PASS.equals(CNFPASS) && PASS.length()>=6)
//                {
//                    mAuth.createUserWithEmailAndPassword(EMAIL,PASS).addOnCompleteListener(task -> {
//                        if (task.isSuccessful())
//                        {
//                            InputStream stream = null;
//                            try {
//                                stream = new FileInputStream(ImagePath);
//                            } catch (FileNotFoundException e) {
//                                throw new RuntimeException(e);
//                            }
//                            storageReference.child("display_picture").child(mAuth.getCurrentUser().getUid()).putStream(stream);
//                            download_url = "https://firebasestorage.googleapis.com/v0/b/justchat-253f8.appspot.com/o/display_picture%2F"+mAuth.getCurrentUser().getUid()+"?alt=media";
//                            dbref = FirebaseDatabase.getInstance().getReference();
//                            dbref.child("user").child(mAuth.getCurrentUser().getUid()).setValue(new User(NAME,EMAIL,mAuth.getCurrentUser().getUid(),download_url));
//                            Intent intent = new Intent(SignUp.this,MainActivity.class);
//                            finish();
//                            startActivity(intent);
//                        }
//                        else Toast.makeText(SignUp.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
//                    });
//                }
//            }
//        });
    }
    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
}