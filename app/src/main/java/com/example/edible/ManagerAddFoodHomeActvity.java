package com.example.edible;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ManagerAddFoodHomeActvity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ManagerFoodsAdapter adapter;
    private List<ManagerFoodsModal> mFoodList = new ArrayList<>();
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private static final String TAG = "ManagerAddFoodHomeActvi";
    private String mRestaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_food_home_actvity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Added Foods");

        mRecyclerView = findViewById(R.id.manager_view_food_recyclerView);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            mRestaurantName = documentSnapshot.getString("managerFor");

                            mFirestore.collection("Restaurants").document(mRestaurantName).collection("FoodMenu")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.d(TAG, "onEvent: SignedOut");
                                            } else {
                                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                                        ManagerFoodsModal managerFoodsModal = doc.getDocument().toObject(ManagerFoodsModal.class);
                                                        mFoodList.add(managerFoodsModal);
                                                        adapter.notifyDataSetChanged();

                                                    }
                                                }
                                            }
                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ManagerAddFoodHomeActvity.this, "Failed to load managerFor", Toast.LENGTH_LONG).show();
            }
        });


        adapter = new ManagerFoodsAdapter(ManagerAddFoodHomeActvity.this,mFoodList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerAddFoodHomeActvity.this, ManagerAddFoodActivity.class);
                startActivity(intent);
            }
        });
    }



}
