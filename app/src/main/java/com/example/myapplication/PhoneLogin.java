package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneLogin extends AppCompatActivity {

    private EditText phoneNumber, verificationCode;
    private ImageView pass,man;
    private Button loginByPhone, sendCode;
    private ProgressDialog loadingBar;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_phone_login);


        loginByPhone = findViewById(R.id.verify);
        sendCode = findViewById(R.id.sendCode);
        backButton = findViewById(R.id.backButton);
        phoneNumber = findViewById(R.id.phoneNumber);
        verificationCode = findViewById(R.id.code);
        pass = findViewById(R.id.pass);
        man = findViewById(R.id.man);

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

            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode.setVisibility(View.INVISIBLE);
                phoneNumber.setVisibility(View.INVISIBLE);
                man.setVisibility(View.INVISIBLE);
                loginByPhone.setVisibility(View.VISIBLE);
                verificationCode.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);

            }
        });

        loadingBar =  new ProgressDialog(this);

        loginByPhone.setVisibility(View.INVISIBLE);
        verificationCode.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);





    }

}
