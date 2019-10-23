package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar mLoginProgressBar;
    private EditText mLoginEmailEditText, mLoginPasswordEditText;
    private Button mLoginBtn, mLoginSignupBtn,mForgotPasswordBtn;
    FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginProgressBar = findViewById(R.id.signup_progress_bar);
        mLoginEmailEditText = findViewById(R.id.signup_email_edittext);
        mLoginPasswordEditText = findViewById(R.id.signup_password_edittext);
        mLoginBtn = findViewById(R.id.register_signup_btn);
        mLoginSignupBtn = findViewById(R.id.login_signup_btn);
        mForgotPasswordBtn = findViewById(R.id.forgot_password_btn);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mLoginEmailEditText.getText().toString().trim();
                String password = mLoginPasswordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mLoginEmailEditText.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mLoginPasswordEditText.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }

                if(password.length() < 6){
                    mLoginPasswordEditText.setError(getString(R.string.password_length));
                    return;
                }

                mLoginProgressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mLoginProgressBar.setVisibility(View.INVISIBLE);
                                if(task.isSuccessful()){

                                    mFirestore.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot != null){
                                            String type = documentSnapshot.getString("type");
                                            if(type.equals("user")){
                                                Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                            else if(type.equals("manager")){

                                                Intent managerIntent = new Intent(LoginActivity.this,ManagerHomeActivity.class);
                                                managerIntent.putExtra("username",email);
                                                startActivity(managerIntent);
                                                finish();
                                            }
                                            else if(type.equals("admin")){
                                                Intent realAdminIntent = new Intent(LoginActivity.this,AdminActivity.class);
                                                startActivity(realAdminIntent);
                                                finish();
                                            }
                                        }
                                        }
                                    });

                                }
                                else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordIntent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(resetPasswordIntent);
            }
        });

        mLoginSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(signUpIntent);

            }
        });
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent mainIntent = new Intent(this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
