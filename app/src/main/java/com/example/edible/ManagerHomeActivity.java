package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ManagerHomeActivity extends AppCompatActivity {

    private Button mCheckPendingOrderBtn,mAddFoodBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mCheckPendingOrderBtn = findViewById(R.id.check_pending_order_btn);
        mAddFoodBtn = findViewById(R.id.add_food_btn);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        final DocumentReference docRef = mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                            return;
                    }
                    else{

                        mFirestore.collection("Users").document(username).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            String restaurantName = documentSnapshot.getString("managerFor");

                                             Map<String,String> managerForMap = new HashMap<>();
                                            managerForMap.put("managerFor",restaurantName);
                                            managerForMap.put("username",username);
                                            mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                                    .set(managerForMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                    Toast.makeText(ManagerHomeActivity.this,"Successfully added managerFor credential to database",Toast.LENGTH_LONG).show();
                                                }else{
                                                        Toast.makeText(ManagerHomeActivity.this,"Failed to add managerFor credential to Database",Toast.LENGTH_LONG);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                    }
                }
            }
        });





    mAddFoodBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ManagerHomeActivity.this,ManagerAddFoodHomeActvity.class);
            startActivity(intent);
        }
    });

    mCheckPendingOrderBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(ManagerHomeActivity.this,MangerPendingOrderActivity.class);
            startActivity(intent1);
        }
    });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
        finish();
    }
}
