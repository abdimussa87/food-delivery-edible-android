package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AccountSettings extends AppCompatActivity {

    private TextView mUsernameTv;
    private TextView mPasswordTv;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        mUsernameTv = findViewById(R.id.username_tv);
        mPasswordTv = findViewById(R.id.password_tv);

        setTitle("Account Settings");
    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String uId = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(uId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String username = documentSnapshot.getString("username");
                    String password = documentSnapshot.getString("password");
                    mUsernameTv.setText(username);
                    mPasswordTv.setText(password);

                }
            }) ;
        }

    }
}
