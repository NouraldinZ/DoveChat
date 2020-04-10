package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gender1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender1);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGender2();
            }
        });
    }

    private void openGender2() {
        Intent intent = new Intent(Gender1.this,Gender2.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);

    }
}
