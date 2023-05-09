package com.level_zero.greeniq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompat {

    private TextInputLayout email, password, userName, location, phoneNumber;
    private FirebaseAuth firebaseAuth;
    private String fetchEmail, fetchUsername, fetchPassword, fetchPhone, fetchLocation;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference, databaseReferenceCarbon, databaseReferenceCertificate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.level_zero.greeniq.databinding.ActivityRegisterBinding binding
                = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");
        databaseReferenceCarbon = firebaseDatabase.getReference("Carbon Data");
        databaseReferenceCertificate = firebaseDatabase.getReference("Certificate");

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.userPassword);
        AppCompatButton signUp = findViewById(R.id.signupButton);
        userName = findViewById(R.id.userUserName);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        location = findViewById(R.id.userLocation);

        signUp.setOnClickListener(view -> {
            ProgressBar progressBar = findViewById(R.id.loading);
            progressBar.setVisibility(View.VISIBLE);

            registerUser();

            progressBar.setVisibility(View.GONE);
        });
    }

    private void registerUser() {
        fetchEmail = email.getEditText().getText().toString();
        fetchUsername = userName.getEditText().getText().toString();
        fetchPassword = password.getEditText().getText().toString();
        fetchPhone = phoneNumber.getEditText().getText().toString();
        fetchLocation = location.getEditText().getText().toString();

        if(fetchEmail.equals("") || fetchPassword.equals("") || fetchUsername.equals("") || fetchLocation.equals("") || fetchPhone.equals("")){
            email.setError("All fields must not empty");
            userName.setError("All fields must not empty");
            password.setError("All fields must not empty");
            phoneNumber.setError("All fields must not empty");
            location.setError("All fields must not empty");
        } else{
            email.setError(null);
            userName.setError(null);
            password.setError(null);
            phoneNumber.setError(null);
            location.setError(null);

            databaseReference.orderByChild("userName").equalTo(fetchUsername)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                userName.setError("Username already exists.");
                            }else{
                                userName.setError(null);
                                if(fetchEmail.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")){
                                    if(fetchPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")){
                                        if(fetchPhone.matches("^(09|\\+639)\\d{9}$")){
                                            try {
                                                firebaseAuth.createUserWithEmailAndPassword(fetchEmail, fetchPassword).addOnCompleteListener(task -> {
                                                    if(task.isSuccessful()){
                                                        FirebaseUser user = task.getResult().getUser();
                                                        String userId = Objects.requireNonNull(user).getUid();
                                                        String emptyCert = "https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2FUntitled-1.jpg?alt=media&token=5d9cb3f7-af0c-45a5-937c-4bc4520e6574";
                                                        String defaultProfile = "https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/images%2F1e98af88-102c-4ffd-a85c-c450162cd7d7?alt=media&token=d1d09296-b020-422b-a584-2fb40719bb66";
                                                        databaseReference.child(userId).setValue(new Profile(fetchUsername, fetchPhone, defaultProfile, fetchLocation, fetchEmail, fetchPassword, userId, "0"));
                                                        databaseReferenceCarbon.child(userId).setValue(new CarbonData("1", "1", "1"));
                                                        databaseReferenceCertificate.child(fetchUsername).child("default").setValue(new Certificate(emptyCert));
                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                        Toast.makeText(getApplicationContext(),"Account registered for , "+fetchUsername+"!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } catch (NullPointerException e){
                                                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            } catch (IllegalArgumentException e){
                                                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
                                            }
                                            phoneNumber.setError(null);
                                        }else{
                                            phoneNumber.setError("Invalid Phone number!");
                                        }
                                        password.setError(null);
                                    }else{
                                        password.setError("Should have one uppercase and lowercase character, one digit number, and one special character.");
                                    }
                                    email.setError(null);
                                }else{
                                    email.setError("Invalid Email!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }


    public void openLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}