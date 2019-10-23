package com.example.edible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MangerPendingOrderActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private static String managerUsername;
    private static final String TAG = "MangerPendingOrderActiv";
    private RecyclerView mRecyclerView;
    private List<PendingOrderModal> mPendFoodList = new ArrayList<>();
    private PendingOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_pending_order);
        setTitle("Pending Orders");

        mRecyclerView = findViewById(R.id.pending_order_recyclerView);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        //getting manager username
        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            managerUsername = documentSnapshot.getString("username");

                            //
                            mFirestore.collection("Users").document(managerUsername).collection("Orders")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                            if (e != null) {
                                                Log.d(TAG, "onEvent: " + e.getMessage());
                                            } else {
                                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                                    if (documentChange.getType() == DocumentChange.Type.ADDED ) {
                                                        PendingOrderModal pendingOrderModal = documentChange.getDocument().toObject(PendingOrderModal.class).withID(documentChange.getDocument().getId());
                                                        mPendFoodList.add(pendingOrderModal);
                                                        adapter.notifyDataSetChanged();
                                                    }else if(documentChange.getType() == DocumentChange.Type.REMOVED){
                                                        mPendFoodList.remove(documentChange.getOldIndex());
                                                        adapter.notifyItemRemoved(documentChange.getOldIndex());
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

        adapter = new PendingOrderAdapter(MangerPendingOrderActivity.this, mPendFoodList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        // for done btn
        adapter.setOnItemClickListener(new PendingOrderAdapter.onItemClickListener() {
            @Override
            public void onDoneClicked(final String docPostition) {
                // getting orderer UserId and foodName
                mFirestore.collection("Users").document(managerUsername).collection("Orders").document(docPostition).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String ordererUserId = documentSnapshot.getString("orderedBy");
                                    String foodName = documentSnapshot.getString("foodName");
                                    // going into order of the orderer and changing processed to processed(true)
                                    Map<String, String> processedMap = new HashMap<>();
                                    processedMap.put("processed", "processed");
                                    mFirestore.collection("Users").document(ordererUserId).collection("Orders").document(foodName).update("processed", "Processed")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MangerPendingOrderActivity.this, "Processed Successfully", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                    //deleting order from manager Orders collection
                                    mFirestore.collection("Users").document(managerUsername).collection("Orders").document(docPostition).delete();
                                }

                            }
                        });

            }
        });
    }
}