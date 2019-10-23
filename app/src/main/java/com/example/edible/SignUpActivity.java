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

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private ProgressBar mSignUpProgressBar;
    private Button mSigupBtn, mAlreadyHaveAccountBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailEditText = findViewById(R.id.signup_email_edittext);
        mPasswordEditText = findViewById(R.id.signup_password_edittext);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        mSignUpProgressBar = findViewById(R.id.signup_progress_bar);
        mSigupBtn = findViewById(R.id.register_signup_btn);
        mAlreadyHaveAccountBtn = findViewById(R.id.signup_already_have_an_account_btn);

        mAuth = FirebaseAuth.getInstance();
        mSigupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });
        mAlreadyHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });
    }

    private void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.register_signup_btn:
                final String email = mEmailEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmailEditText.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPasswordEditText.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    mPasswordEditText.setError(getString(R.string.password_length));
                    return;
                }
                mSignUpProgressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                    mainIntent.putExtra("username",email);
                                    mainIntent.putExtra("password",password);
                                    startActivity(mainIntent);
                                    finish();
                                } else {

                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                mSignUpProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                break;
            case R.id.signup_already_have_an_account_btn:
                finish();

        }
    }
}
