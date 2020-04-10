package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseUser currentUser;
    private EditText emailAddress, password;
    private Button createAccount;
    private ImageView backButton;
    private FirebaseAuth authenticate;
    private ProgressDialog loadingBar;
    private DatabaseReference rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        FirebaseApp.initializeApp(this);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);

        createAccount = findViewById(R.id.createAccount);

        backButton = findViewById(R.id.backButton);

        loadingBar = new ProgressDialog(this);

        authenticate = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Register.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        String email = emailAddress.getText().toString();
        String passwordS = password.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(passwordS)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait, while we create new credentials for you ...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            authenticate.createUserWithEmailAndPassword(email,passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String currentUserID = authenticate.getInstance().getUid();
                        rootRef.child("Users").child(currentUserID).setValue(""); //


                        Intent loginIntent = new Intent(Register.this, Messenger.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();

                        Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss(); //dismissing loading bar, account creation succeeded.
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(Register.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss(); //dismissing loading bar, account creation failed.
                    }
                }
            });


        }



    }
}
