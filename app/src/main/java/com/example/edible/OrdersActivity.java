package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView mOrderRecyclerView;
    private String managerUsername;
    private String userUsername;
    private String foodImageUri;
    private String foodName;
    static String restaurantName;
    static String restaurantName2;
    private static String orderedFoodName;
    private String foodPrice;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private static String processed;
    private OrderAdapter adapter;
    private static final String TAG = "OrdersActivity";
    private List<OrderModal> mOrderList  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setTitle("Orders");

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mOrderRecyclerView = findViewById(R.id.orders_recycler_view);

        final Intent intent = getIntent();
        if(intent.hasExtra("order") && intent.hasExtra("restaurant")) {
            if (intent.getStringExtra("restaurant") != null) {

                final String foodMenu = intent.getStringExtra("order");
                  restaurantName = intent.getStringExtra("restaurant");


                DocumentReference docRef = mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").document(foodMenu);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Toast.makeText(OrdersActivity.this,"Please remove your previous order of same food type", Toast.LENGTH_LONG).show();
                                return;
                            }else{
                                mFireStore.collection("Restaurants").document(restaurantName).collection("FoodMenu").document(foodMenu).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    final DocumentSnapshot documentSnapshot = task.getResult();
                                                    foodImageUri = documentSnapshot.getString("foodImageUri");
                                                    foodName = documentSnapshot.getString("foodName");
                                                    foodPrice = documentSnapshot.getString("foodPrice");

                                                    Map<String, String> orderMap = new HashMap<>();
                                                    orderMap.put("foodImageUri", foodImageUri);
                                                    orderMap.put("foodName", foodName);
                                                    orderMap.put("foodPrice", foodPrice);
                                                    orderMap.put("processed","");
                                                    orderMap.put("restaurantName",restaurantName);

                                                    mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").document(foodMenu).set(orderMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(OrdersActivity.this, "Successfully added order ", Toast.LENGTH_LONG).show();
                                                                    } else {
                                                                        Toast.makeText(OrdersActivity.this, "Failed to add order to firestore", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                    // getting restaurant manager username and adding order to there

                                                    mFireStore.collection("Restaurants").document(restaurantName).get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.isSuccessful()){
                                                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                                                        managerUsername = documentSnapshot1.getString("restaurantManager");
                                                                        final Map<String,String> orderManagerMap = new HashMap<>();
                                                                        // to get user's username through it's user id
                                                                        mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                        if (task.isSuccessful()){
                                                                                            DocumentSnapshot documentSnapshot2 = task.getResult();
                                                                                            userUsername = documentSnapshot2.getString("username");


                                                                                            orderManagerMap.put("foodName",foodName);
                                                                                            orderManagerMap.put("foodPrice",foodPrice);
                                                                                            orderManagerMap.put("ordererUsername",userUsername);
                                                                                            orderManagerMap.put("orderedBy",mAuth.getCurrentUser().getUid());
                                                                                            orderManagerMap.put("uniqueIndex",foodName + mAuth.getCurrentUser().getUid());
                                                                                            //adding order to restaurant manager username
                                                                                            mFireStore.collection("Users").document(managerUsername).collection("Orders").add(orderManagerMap)
                                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                                                            //Toast.makeText(OrdersActivity.this,"added order to manager username",Toast.LENGTH_LONG).show();
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });

                                                                    }
                                                                }
                                                            });




                                                }
                                                else {
                                                    Toast.makeText(OrdersActivity.this,"Eror",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });


            }
        }

        mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }else {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            OrderModal orderModal = doc.getDocument().toObject(OrderModal.class).withID(doc.getDocument().getId());
                            mOrderList.add(orderModal);
                            adapter.notifyDataSetChanged();
                        }else if(doc.getType() == DocumentChange.Type.REMOVED){
                            mOrderList.remove(doc.getOldIndex());
                            adapter.notifyItemRemoved(doc.getOldIndex());
                        }
                    }
                }
            }
        });

        adapter = new OrderAdapter(OrdersActivity.this,mOrderList);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderRecyclerView.setAdapter(adapter);

        //for remove order button
        adapter.setOnItemClickListener(new OrderAdapter.onItemClickListener() {
            @Override
            public void onRemoveClicked(String docPostition) {
                //getting processed value
                mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").document(docPostition).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    final DocumentSnapshot documentSnapshot = task.getResult();
                                    processed = documentSnapshot.getString("processed");
                                    orderedFoodName = documentSnapshot.getString("foodName");
                                    restaurantName2 = documentSnapshot.getString("restaurantName");
                                    if(processed.equals("")){
                                        mFireStore.collection("Restaurants").document(restaurantName2).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()) {
                                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                                            String managerUsername2 = documentSnapshot1.getString("restaurantManager");
                                                            Query documentQuery = mFireStore.collection("Users").document(managerUsername2).collection("Orders").whereEqualTo("uniqueIndex",orderedFoodName +mAuth.getCurrentUser().getUid());
                                                            documentQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                                    for(DocumentSnapshot documentSnapshot2:queryDocumentSnapshots.getDocuments()){
                                                                        DocumentReference documentReference = documentSnapshot2.getReference();
                                                                        documentReference.delete();
                                                                    }
                                                                }
                                                            });


                                                            mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").document(orderedFoodName).delete();
                                                        }
                                                    }
                                                });
                                    }else {
                                        mFireStore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Orders").document(orderedFoodName).delete();

                                    }
                                }
                            }
                        });

            }
        });
    }
}
