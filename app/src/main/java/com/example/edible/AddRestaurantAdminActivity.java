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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class AddRestaurantAdminActivity extends AppCompatActivity {
    private static final String TAG = "AddRestaurantAdminActiv";
    private ImageView  add_restaurant_imageview;
    private EditText restaurant_name_edittext, restaurant_location_edittext, manager_username_edittext,manager_password_edittext;
    private Button add_restaurant_btn;
    private EditText restaurantDesccripitonEditText;
    private ProgressBar addRestaurantProgressBar;

    private Uri restaurantImageUri = null;

    private StorageReference mStorageReference;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_admin);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        restaurantDesccripitonEditText = findViewById(R.id.food_content_edittext);

        addRestaurantProgressBar = findViewById(R.id.add_food_progressBar);
        add_restaurant_imageview = findViewById(R.id.add_food_imageview);
        restaurant_name_edittext = findViewById(R.id.food_name_edittext);
        manager_username_edittext = findViewById(R.id.manager_username_edittext);
        manager_password_edittext = findViewById(R.id.manager_password_edittext);
        restaurant_location_edittext = findViewById(R.id.food_price_edittext);
        add_restaurant_btn = findViewById(R.id.add_food_btn);

        add_restaurant_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(AddRestaurantAdminActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(AddRestaurantAdminActivity.this,"Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(AddRestaurantAdminActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} ,1);
                    }
                    else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(AddRestaurantAdminActivity.this);
                    }
                }else{
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(AddRestaurantAdminActivity.this);
                }
            }
        });

        add_restaurant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String restaurantName = restaurant_name_edittext.getText().toString();
                final String restaurantLocation = restaurant_location_edittext.getText().toString();
                final String managerUsername = manager_username_edittext.getText().toString();
                final String managerPassword = manager_password_edittext.getText().toString();
                final String restaurantDesc = restaurantDesccripitonEditText.getText().toString();
                if (TextUtils.isEmpty(restaurantName)) {
                    restaurant_name_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if (TextUtils.isEmpty(restaurantLocation)) {
                    restaurant_location_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if (TextUtils.isEmpty(managerUsername)) {
                    manager_username_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }
                if(TextUtils.isEmpty(restaurantDesc)){
                    restaurantDesccripitonEditText.setError("Field can not be empty");
                    return;
                }
                if(!managerUsername.contains("@")){
                    manager_username_edittext.setError("Invalid email pattern");
                    return;
                }

                if(!managerUsername.endsWith(".com")){
                    manager_username_edittext.setError("Invalid email pattern");
                    return;
                }

                if (TextUtils.isEmpty(managerPassword)) {
                    manager_password_edittext.setError(getString(R.string.field_can_not_be_empty));
                    return;
                }

                if(managerPassword.length() <6){
                    manager_password_edittext.setError(getString(R.string.password_length));
                    return;

                }
                if(restaurantImageUri == null){
                    Toast.makeText(AddRestaurantAdminActivity.this,"Please select an image", Toast.LENGTH_LONG).show();
                    return;
                }
                addRestaurantProgressBar.setVisibility(View.VISIBLE);
                final StorageReference image_path = mStorageReference.child("Restaurants").child(restaurantName + "jpg");
                UploadTask uploadTask = image_path.putFile(restaurantImageUri);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return image_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            Map<String,String> restaurantMap = new HashMap<>();
                            restaurantMap.put("restaurantName",restaurantName);
                            restaurantMap.put("restaurantDescription",restaurantDesc);
                            restaurantMap.put("restaurantLocation",restaurantLocation);
                            restaurantMap.put("restaurantManager",managerUsername);
                            restaurantMap.put("adminRestaurantImageUrl",downloadUri.toString());
                            mFireStore.collection("Restaurants").document(restaurantName).set(restaurantMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddRestaurantAdminActivity.this,"Successfully added",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddRestaurantAdminActivity.this,"Error of firestore" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

                            mAuth.createUserWithEmailAndPassword(managerUsername,managerPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                            Map<String,String> userMap = new HashMap<>();
                                            userMap.put("username",managerUsername);
                                            userMap.put("password",managerPassword);
                                            userMap.put("managerFor",restaurantName);
                                            userMap.put("type","manager");
                                            mFireStore.collection("Users").document(managerUsername).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Intent adminIntent = new Intent(AddRestaurantAdminActivity.this,AdminActivity.class);
                                                    startActivity(adminIntent);
                                                    finish();
                                                }
                                            });
                                        }
                                            else{
                                                addRestaurantProgressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(AddRestaurantAdminActivity.this,"Failed to add manager credential to Firestore collection Users",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    });
                                addRestaurantProgressBar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            addRestaurantProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddRestaurantAdminActivity.this,"Errorr",Toast.LENGTH_SHORT).show();
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
                restaurantImageUri = result.getUri();
                add_restaurant_imageview.setImageURI(restaurantImageUri);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception eror = result.getError();
                Toast.makeText(AddRestaurantAdminActivity.this,"crop Image Error: " + eror.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
