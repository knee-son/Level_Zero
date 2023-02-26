package com.level_zero.greeniq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.level_zero.greeniq.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout email, password, userName, location, phoneNumber;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.level_zero.greeniq.databinding.ActivityRegisterBinding binding
                = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        AppCompatButton signUp = findViewById(R.id.signupButton);
        userName = findViewById(R.id.userUserName);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        location = findViewById(R.id.userLocation);

        //signUp.setOnClickListener(view -> registerUser());

        signUp.setOnClickListener(view -> {
            if(validateField(email)){
                email.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                email.setError(null);
                email.setErrorEnabled(false);
                registerUser();
            }
            if(validateField(password)){
                password.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                password.setError(null);
                password.setErrorEnabled(false);
                registerUser();
            }
            if(validateField(userName)){
                userName.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                userName.setError(null);
                userName.setErrorEnabled(false);
                registerUser();
            }
            if(validateField(location)){
                location.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                location.setError(null);
                location.setErrorEnabled(false);
                registerUser();
            }
            if(validateField(phoneNumber)){
                phoneNumber.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                phoneNumber.setError(null);
                phoneNumber.setErrorEnabled(false);
                registerUser();
            }
        });
    }

    public static String safeFetch(TextInputLayout l){
        return Objects.requireNonNull(l.getEditText()).getText().toString();
    }

    private void registerUser() {
        try {
            String userEmail = safeFetch(email);
            String userPass = safeFetch(password);
            String userUserName = safeFetch(userName);
            String userPhoneNumber = safeFetch(phoneNumber);
            String userLocation = safeFetch(location);

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    String userId = Objects.requireNonNull(user).getUid();
                    String defaultProfile = "https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/images%2F1e98af88-102c-4ffd-a85c-c450162cd7d7?alt=media&token=d1d09296-b020-422b-a584-2fb40719bb66";
                    databaseReference.child(userId).setValue(new Profile(userUserName, userPhoneNumber, defaultProfile, userLocation, userEmail, userPass, userId));
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Toast.makeText(getApplicationContext(),"You are now registered, "+userUserName+"!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateField(TextInputLayout field){
        String val = Objects.requireNonNull(field.getEditText()).getText().toString();

        return val.isEmpty();
    }

    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}