package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private ImageView backButton, profileCover;
    private Button updateButton;
    private EditText username, status;
    private CircleImageView profileImage;
    private ProgressDialog loadingBar;
    private FirebaseAuth authenticate;
    private DatabaseReference rootRef;
    private String currentUserID;
    private Boolean isCover = false;

    private static final int galleryPick = 1;
    private StorageReference userProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        updateButton = findViewById(R.id.updateButton);
        username = findViewById(R.id.username);
        status = findViewById(R.id.status);
        profileImage = findViewById(R.id.profileImage);

        loadingBar = new ProgressDialog(this);

        authenticate = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = authenticate.getCurrentUser().getUid();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        profileCover = findViewById(R.id.profile_cover);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateSettings();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SettingsActivity.this, Messenger.class);
                startActivity(loginIntent);
            }
        });

        RetrieveUserInfo();

        profileCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

                isCover = true;

            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK && isCover) {

                    loadingBar.setTitle("Set Profile Cover");
                    loadingBar.setMessage("Please wait your cover is updating...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    Uri resultUri = result.getUri();

                    StorageReference filePath = userProfileImageRef.child(currentUserID + "_cover" + ".jpg");


                    filePath.putFile(resultUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final String downloadUrl = uri.toString();

                                            Toast.makeText(SettingsActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();

                                            rootRef.child("Users").child(currentUserID).child("cover")
                                                    .setValue(downloadUrl)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(SettingsActivity.this, "Cover Image saved in database successfully", Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();
                                                                Glide.with(SettingsActivity.this).load(downloadUrl).error(R.drawable.profile_image).into(profileCover);
                                                                isCover = false;
                                                            }
                                                            else{
                                                                String message = task.getException().toString();
                                                                Toast.makeText(SettingsActivity.this, "Error: " + message,Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();
                                                                isCover = false;
                                                            }

                                                        }
                                                    });

                                        }
                                    });

                                }
                            });

            }

                else if (resultCode == RESULT_OK) {

                    loadingBar.setTitle("Set Profile Image");
                    loadingBar.setMessage("Please wait your image is updating...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    Uri resultUri = result.getUri();

                    StorageReference filePath = userProfileImageRef.child(currentUserID + ".jpg");


                    filePath.putFile(resultUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final String downloadUrl = uri.toString();

                                            Toast.makeText(SettingsActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();

                                            rootRef.child("Users").child(currentUserID).child("image")
                                                    .setValue(downloadUrl)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(SettingsActivity.this, "Image saved in database successfully", Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();
                                                                Glide.with(SettingsActivity.this).load(downloadUrl).error(R.drawable.profile_image).into(profileImage);
                                                            }
                                                            else{
                                                                String message = task.getException().toString();
                                                                Toast.makeText(SettingsActivity.this, "Error: " + message,Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();

                                                            }

                                                        }
                                                    });

                                        }
                                    });

                                }
                            });
                }

        }}





    private void RetrieveUserInfo() {

        rootRef.child("Users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status" ) && dataSnapshot.hasChild("image") && dataSnapshot.hasChild("cover") ){

                String retrievedUsername = dataSnapshot.child("name").getValue().toString();
                String retrievedStatus = dataSnapshot.child("status").getValue().toString();
                String retrievedProfileImage = dataSnapshot.child("image").getValue().toString();
                String retrievedProfileCover = dataSnapshot.child("cover").getValue().toString();

                username.setText(retrievedUsername);
                status.setText(retrievedStatus);

                Glide.with(SettingsActivity.this).load(retrievedProfileImage).into(profileImage);
                Glide.with(SettingsActivity.this).load(retrievedProfileCover).into(profileCover);
                //trievedProfileImage).placeholder(R.drawable.profile_image).noFade().fit().error(R.drawable.ic_launcher_background).into(profileImage);
                }
                else if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status" ) && dataSnapshot.hasChild("image") ){

                    String retrievedUsername = dataSnapshot.child("name").getValue().toString();
                    String retrievedStatus = dataSnapshot.child("status").getValue().toString();
                    String retrievedProfileImage = dataSnapshot.child("image").getValue().toString();


                    username.setText(retrievedUsername);
                    status.setText(retrievedStatus);

                    Glide.with(SettingsActivity.this).load(retrievedProfileImage).into(profileImage);

                    //trievedProfileImage).placeholder(R.drawable.profile_image).noFade().fit().error(R.drawable.ic_launcher_background).into(profileImage);
                }
                else if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("status" ) ){

                    String retrievedUsername = dataSnapshot.child("name").getValue().toString();
                    String retrievedStatus = dataSnapshot.child("status").getValue().toString();

                    username.setText(retrievedUsername);
                    status.setText(retrievedStatus);

                }
                else {
                    Toast.makeText(SettingsActivity.this, "Please set & update your account settings", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateSettings() {

        final String usernameS = username.getText().toString();
        final String statusS = status.getText().toString();

        rootRef.child("Users").child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("name").exists())) {

                    if (TextUtils.isEmpty(usernameS)) {
                        Toast.makeText(SettingsActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    }

                    else if (TextUtils.isEmpty(statusS)) {
                        Toast.makeText(SettingsActivity.this, "Please enter a status", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        loadingBar.setTitle("Updating Settings");
                        loadingBar.setMessage("Please Wait, while we updating your account settings ...");
                        loadingBar.setCanceledOnTouchOutside(true);
                        loadingBar.show();

                        HashMap<String,String> profileMap = new HashMap<>();

                        profileMap.put("uid", currentUserID);
                        profileMap.put("name", usernameS);
                        profileMap.put("status", statusS);

                        rootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    SendUserToMessengerActivity();
                                    Toast.makeText(SettingsActivity.this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss(); //dismissing loading bar, account creation succeeded.
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(SettingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss(); //dismissing loading bar, account creation failed.
                                }
                            }
                        });

                    }
                }

                else{

                    if (TextUtils.isEmpty(usernameS)) {
                        Toast.makeText(SettingsActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    }

                    else if (TextUtils.isEmpty(statusS)) {
                        Toast.makeText(SettingsActivity.this, "Please enter a status", Toast.LENGTH_SHORT).show();
                    }

                    else{
                    rootRef.child("Users").child(currentUserID).child("name").setValue(usernameS);
                    rootRef.child("Users").child(currentUserID).child("status").setValue(statusS).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    SendUserToMessengerActivity();
                                    Toast.makeText(SettingsActivity.this, "Account Updated Successfully", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss(); //dismissing loading bar, account creation succeeded.
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(SettingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss(); //dismissing loading bar, account creation failed.
                                }
                            }
                        });

                }}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void SendUserToMessengerActivity() {

        Intent messengerIntent = new Intent(SettingsActivity.this, Messenger.class);
        messengerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(messengerIntent);
        finish();

    }
}
