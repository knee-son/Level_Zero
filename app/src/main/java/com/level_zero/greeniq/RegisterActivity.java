package com.level_zero.greeniq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.level_zero.greeniq.databinding.ActivityMainBinding;
import com.level_zero.greeniq.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

//    private TextInputLayout email, password;
    private AppCompatButton signUp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.level_zero.greeniq.databinding.ActivityRegisterBinding binding
                = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_register);
        System.out.println("wazzap");

        findViewById(R.id.signupButton).setOnClickListener(view -> {
            // TODO: 05/02/2023 shortify this thing: 
            String email = Objects.requireNonNull(binding.userEmail.getEditText().getText()).toString();
            String password = Objects.requireNonNull(binding.userPassword.getEditText().getText()).toString();

            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            System.out.println("*click*");
            System.out.println("email: " + email);
            System.out.println("password: " + password);

            registerUser(email, password);
        });
    }

    private void registerUser(String email, String password) {
//        String userEmail = email.toString().trim();
//        String userPass = password.toString().trim();

        try{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Registration is Complete", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Registration is not Complete", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch(IllegalArgumentException e){
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}