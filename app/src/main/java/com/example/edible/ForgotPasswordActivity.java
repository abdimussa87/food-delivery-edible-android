package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText mForgotPasswordEditText;
    private Button mResetPasswordBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mForgotPasswordEditText  = findViewById(R.id.forgot_password_email_edittext);
        mResetPasswordBtn = findViewById(R.id.reset_password_btn);
        mAuth = FirebaseAuth.getInstance();

        mResetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = mForgotPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mForgotPasswordEditText.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }


                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, R.string.reset_password_successful, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.reset_password_unsuccesful), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            }
        });
    }
}
