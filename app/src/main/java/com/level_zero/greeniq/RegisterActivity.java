package com.level_zero.greeniq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.level_zero.greeniq.databinding.ActivityMainBinding;
import com.level_zero.greeniq.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private AppCompatButton signUp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.level_zero.greeniq.databinding.ActivityRegisterBinding binding
                = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_register);

        binding.signupButton.setOnClickListener (v -> {
            String email = Objects.requireNonNull(binding.userEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.userPassword.getText()).toString();
//
//                mAuth = FirebaseAuth.getInstance();
//                mUser = mAuth.getCurrentUser();

//                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password);
        });
    }
}