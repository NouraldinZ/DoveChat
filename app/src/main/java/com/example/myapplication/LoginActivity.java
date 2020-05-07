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

public class LoginActivity extends AppCompatActivity {

    private EditText emailAddress, password;
    private Button login, phoneLogin;
    private TextView forgetPassword, signUp;
    private ImageView backButton;
    private ProgressDialog loadingBar;
    private FirebaseAuth authenticate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.loginButton);
        phoneLogin = findViewById(R.id.phoneLoginButton);

        forgetPassword = findViewById(R.id.forgetPassword);
        signUp = findViewById(R.id.signup);

        backButton = findViewById(R.id.backButton);

        loadingBar =  new ProgressDialog(this);

        authenticate = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  signUpIntent = new Intent(LoginActivity.this, Register.class);
                startActivity(signUpIntent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneLoginActivityIntent = new Intent(LoginActivity.this, PhoneLogin.class);
                startActivity(phoneLoginActivityIntent);
            }
        });



    }

    private void AllowUserToLogin() {


        String email = emailAddress.getText().toString();
        String passwordS = password.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(passwordS)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }

        else {

            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait, while we validate your credentials ...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            authenticate.signInWithEmailAndPassword(email,passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        SendUsertoMessengerActivity();

                        Toast.makeText(LoginActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss(); //dismissing loading bar, account creation succeeded.
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss(); //dismissing loading bar, account creation failed.
                    }
                }
            });


        }



    }


    private void SendUsertoMessengerActivity() {
        Intent  loginIntent = new Intent(LoginActivity.this, Messenger.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


}
