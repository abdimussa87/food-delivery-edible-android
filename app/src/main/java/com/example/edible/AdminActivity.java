package com.example.edible;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AdminActivity extends AppCompatActivity {

    private List<AdminRestaurantsModal> adminRestaurantList;
    private AdminRestaurantAdapter adapter;
    private FirebaseFirestore mFirestore;
    private RecyclerView adminRecyclerView;
    private FirebaseAuth mAuth;
    private static final String TAG = "AdminActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Admin");

        mAuth = FirebaseAuth.getInstance();
        adminRecyclerView = findViewById(R.id.admin_recyclerview);


        mFirestore = FirebaseFirestore.getInstance();
        adminRestaurantList = new ArrayList<>();
        if(mAuth.getCurrentUser() != null) {
            mFirestore.collection("Restaurants").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if(e!=null){
                        Log.d(TAG, "onEvent: signedout");
                    }else{
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            AdminRestaurantsModal adminRestaurantsModal = doc.getDocument().toObject(AdminRestaurantsModal.class);
                            adminRestaurantList.add(adminRestaurantsModal);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                }
            });
        }

        adapter = new AdminRestaurantAdapter(AdminActivity.this,adminRestaurantList);
        adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminRecyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRestaurantIntent = new Intent(AdminActivity.this,AddRestaurantAdminActivity.class);
                startActivity(addRestaurantIntent);
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
