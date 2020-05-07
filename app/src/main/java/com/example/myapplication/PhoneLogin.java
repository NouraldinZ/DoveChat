package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    private EditText phoneNumber, verificationCodeText;
    private ImageView pass,man;
    private Button loginByPhone, sendCode;
    private ProgressDialog loadingBar;
    private ImageView backButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_phone_login);


        loginByPhone = findViewById(R.id.verify);
        sendCode = findViewById(R.id.sendCode);
        backButton = findViewById(R.id.backButton);
        phoneNumber = findViewById(R.id.phoneNumber);
        verificationCodeText = findViewById(R.id.code);
        pass = findViewById(R.id.pass);
        man = findViewById(R.id.man);

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goBackIntent = new Intent(PhoneLogin.this, LoginActivity.class);
                startActivity(goBackIntent);

            }
        });

        loginByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String verificationCode = verificationCodeText.getText().toString();

                if(TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(PhoneLogin.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();

                }
                else {

                    loadingBar.setTitle("Verifying Code");
                    loadingBar.setMessage("Please Wait, while we verify the code you entered ...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);

                }
            }

        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cellPhoneNumber = phoneNumber.getText().toString();

                if(TextUtils.isEmpty(cellPhoneNumber)){
                    Toast.makeText(PhoneLogin.this, "Please enter a Phone Number", Toast.LENGTH_SHORT).show();

                }
                else {

                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("Please Wait, while we authenticating your phone ...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            cellPhoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLogin.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks

                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                loadingBar.dismiss();
                Toast.makeText(PhoneLogin.this, "Invalid phone Number, Please enter a Valid phone number", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();
                Toast.makeText(PhoneLogin.this, "Verification Code has been sent", Toast.LENGTH_SHORT).show();

                sendCode.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);
                man.setVisibility(View.INVISIBLE);
                loginByPhone.setVisibility(View.VISIBLE);
                verificationCodeText.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);

            }
        };

        loadingBar =  new ProgressDialog(this);

        loginByPhone.setVisibility(View.INVISIBLE);
        verificationCodeText.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
                            Toast.makeText(PhoneLogin.this, "Phone Login Successful!", Toast.LENGTH_SHORT).show();
                            SendUserToMessengerActivity();

                        }

                        else {

                            String message = task.getException().toString();

                            Toast.makeText(PhoneLogin.this, "Error: "+ message, Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void SendUserToMessengerActivity() {
        Intent loginIntent = new Intent(PhoneLogin.this,Messenger.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


}
