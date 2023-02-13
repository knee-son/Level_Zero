package com.level_zero.greeniq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView email, username, phone, location;
    private AppCompatButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.emailTV);
        username = findViewById(R.id.userNameTV);
        phone = findViewById(R.id.phoneNumberTV);
        location = findViewById(R.id.locationTV);

        logout = findViewById(R.id.logoutButton);

        Intent intent = getIntent();

        String userEmail = intent.getStringExtra("email");
        String userUserName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("phoneNumber");
        String userLocation = intent.getStringExtra("location");

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }
}