package com.level_zero.greeniq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView email, username, phone, location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.emailTV);
        username = findViewById(R.id.userNameTV);
        phone = findViewById(R.id.phoneNumberTV);
        location = findViewById(R.id.locationTV);

        Intent intent = getIntent();

        String userEmail = intent.getStringExtra("email");
        String userUserName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("phone");
        String userLocation = intent.getStringExtra("location");

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);
    }
}