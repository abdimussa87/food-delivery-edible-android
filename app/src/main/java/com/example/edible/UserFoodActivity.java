package com.example.edible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserFoodActivity extends AppCompatActivity {

    private RecyclerView mUserFoodRecyclerView;
    private UserFoodAdapter adapter;

    public String documentId;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private FloatingActionButton mLocationBtn;
    private List<UserFoodModal> mUserFoodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food);


        mUserFoodRecyclerView = findViewById(R.id.user_food_recyclerview);
        mLocationBtn = findViewById(R.id.location_fab);

        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();


        documentId = intent.getStringExtra("docId");
        setTitle(documentId);

        mFireStore.collection("Restaurants").document(documentId).collection("FoodMenu").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                } else {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            UserFoodModal userFoodModal = doc.getDocument().toObject(UserFoodModal.class).withID(doc.getDocument().getId());
                            mUserFoodList.add(userFoodModal);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });


        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("google.navigation:q=" + getSupportActionBar().getTitle().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        adapter = new UserFoodAdapter(UserFoodActivity.this, mUserFoodList, documentId);
        mUserFoodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserFoodRecyclerView.setAdapter(adapter);


    }
}
