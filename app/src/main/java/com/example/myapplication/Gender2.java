package com.example.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Gender2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender2);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGender1();
            }
        });
    }

    private void openGender1() {
        Intent intent = new Intent(Gender2.this,Gender1.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }
}
