package com.example.edible;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class ManagerAddFoodActivity extends AppCompatActivity {

    private ImageView add_food_imageview;
    private EditText food_name_edittext,food_price_edittext,food_content_edittext;
    private Button add_food_btn;
    private ProgressBar add_food_progressBar;

    private Uri mFoodImageUri = null;
    private String mManagerFor;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_add_food);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        add_food_imageview = findViewById(R.id.add_food_imageview);
        food_name_edittext = findViewById(R.id.food_name_edittext);
        food_price_edittext = findViewById(R.id.food_price_edittext);
        food_content_edittext = findViewById(R.id.food_content_edittext);
        add_food_btn = findViewById(R.id.add_food_btn);
        add_food_progressBar = findViewById(R.id.add_food_progressBar);

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            mManagerFor = documentSnapshot.getString("managerFor");
                        }
                    }
                });

        add_food_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(ManagerAddFoodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ManagerAddFoodActivity.this,"Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ManagerAddFoodActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} ,1);
                    }
                    else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(ManagerAddFoodActivity.this);
                    }
                }else{
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(ManagerAddFoodActivity.this);
                }
            }
        });

        add_food_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String foodName = food_name_edittext.getText().toString();
                final String foodContent = food_content_edittext.getText().toString();
                final String foodPrice = food_price_edittext.getText().toString();

                if (TextUtils.isEmpty(foodName)) {
                    food_name_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if (TextUtils.isEmpty(foodContent)) {
                    food_content_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if (TextUtils.isEmpty(foodPrice)) {
                    food_price_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;

                }
                if(mFoodImageUri == null){
                    Toast.makeText(ManagerAddFoodActivity.this,"Please select an image", Toast.LENGTH_LONG).show();
                    return;
                }

                add_food_progressBar.setVisibility(View.VISIBLE);
                final StorageReference image_path = mStorageReference.child(mManagerFor).child("Food").child(foodName + "jpg");
                UploadTask uploadTask = image_path.putFile(mFoodImageUri);
                //getting download uri for uploaded image
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return image_path.getDownloadUrl();
            }
                    //adding food menu to restaurant
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            Map<String,String> foodMap = new HashMap<>();
                            foodMap.put("foodName",foodName);
                            foodMap.put("foodContent",foodContent);
                            foodMap.put("foodPrice",foodPrice);
                            foodMap.put("foodImageUri",downloadUri.toString());


                            mFirestore.collection("Restaurants").document(mManagerFor).collection("FoodMenu").document(foodName).set(foodMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ManagerAddFoodActivity.this,"Successfully added food", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ManagerAddFoodActivity.this,ManagerAddFoodHomeActvity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ManagerAddFoodActivity.this,"Failed to add food to firestore",Toast.LENGTH_LONG).show();
                                }
                            });

                            add_food_progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            add_food_progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(ManagerAddFoodActivity.this,"Failed at the task of getting download uri for posted image to storage",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                mFoodImageUri = result.getUri();
                add_food_imageview.setImageURI(mFoodImageUri);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception eror = result.getError();
                Toast.makeText(ManagerAddFoodActivity.this,"crop Image Error: " + eror.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
