package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGender1();
            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDashboard();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessenger();
            }
        });


    }

    private void openMessenger() {
        Intent intent = new Intent(MainActivity.this,Messenger.class);
        startActivity(intent);
    }

    private void openDashboard() {
        Intent intent = new Intent(MainActivity.this,Dashboard.class);
        startActivity(intent);
    }


    private void openGender1() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);

    }

}
