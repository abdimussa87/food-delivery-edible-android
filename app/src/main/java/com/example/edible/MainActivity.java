package com.example.edible;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private List<MainActivityModal> mainActivityModalList;
    private RecyclerView mMainRecyclerView;
    private MainActivityAdapter adapter;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edible");

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // intent data to store user information on database got from sign up activity

        mainActivityModalList = new ArrayList<>();
        mMainRecyclerView = findViewById(R.id.main_recycler_view);

        Intent intent = getIntent();

        if(intent.hasExtra("username")&& intent.hasExtra("password")) {

            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            String uId = mAuth.getCurrentUser().getUid();
            Map<String, String> userMap = new HashMap<>();
            userMap.put("username", username);
            userMap.put("password", password);
            userMap.put("type","user");
            mFirestore.collection("Users").document(uId).set(userMap).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });

            mFirestore.collection("Users").document(username).set(userMap).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String error = e.getMessage();
                    Toast.makeText(MainActivity.this,"Error due to adding to Users using username as document ID " + error ,Toast.LENGTH_SHORT).show();
                }
            });

        }

        if (mAuth.getCurrentUser() != null){
            mFirestore.collection("Restaurants").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e!=null){
                        Log.d(TAG, "onEvent: signedout" + e.getMessage());
                    }else {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                MainActivityModal mainActivityModal = doc.getDocument().toObject(MainActivityModal.class).withID(doc.getDocument().getId());
                                mainActivityModalList.add(mainActivityModal);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
            });
        }

        adapter = new MainActivityAdapter(MainActivity.this,mainActivityModalList);
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mMainRecyclerView.setAdapter(adapter);





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_restaurants) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(this,OrdersActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_my_account) {

            Intent intent = new Intent(MainActivity.this,AccountSettings.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this,About.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
