package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity {

    String username;
    Button startbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText text = findViewById(R.id.username);
        String nn = text.getText().toString();

        username = text.getText().toString(); // string username

        Log.d("Username", "Value: checking--->nnn" + (nn));

        startbutton = findViewById(R.id.button2); // starting button
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ee = findViewById(R.id.username);
                String mm = ee.getText().toString();
                Log.d("Username", "mm---->>" + (mm));

                Intent intent = new Intent(login.this,Berkeley_Bears.class);
                intent.putExtra("pass",mm);
                startActivity(intent);
            }
        });

    }


    private void setOnClickForStartButton() {
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(login.this,Berkeley_Bears.class);
                myintent.putExtra("pass",username);
                startActivity(myintent);


            }
        });
    }
}
