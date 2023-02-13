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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.level_zero.greeniq.databinding.ActivityMainBinding;
import com.level_zero.greeniq.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout email, password, userName, location, phoneNumber;
    private AppCompatButton signUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.level_zero.greeniq.databinding.ActivityRegisterBinding binding
                = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        signUp = findViewById(R.id.signupButton);
        userName = findViewById(R.id.userUserName);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        location = findViewById(R.id.userLocation);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
       String userEmail = email.getEditText().getText().toString();
       String userPass = password.getEditText().getText().toString();
       String userUserName = userName.getEditText().getText().toString();
       String userPhoneNumber = phoneNumber.getEditText().getText().toString();
       String userLocation = location.getEditText().getText().toString();

       firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   FirebaseUser user = task.getResult().getUser();
                   String userId = user.getUid();
                   databaseReference.child(userId).setValue(new Profile(userUserName, userPhoneNumber, "", userLocation, userEmail, userPass, userId));
                   startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                   Toast.makeText(getApplicationContext(),"Registration is Complete", Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(getApplicationContext(),"Registration is not Complete", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}