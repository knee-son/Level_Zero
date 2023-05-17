package com.level_zero.greeniq;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.databinding.FragmentAirQualityBinding;

public class LoginActivity extends AppCompat {

    private TextInputLayout email, password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User");

        email = findViewById(R.id.userEmailLog);
        password = findViewById(R.id.userPasswordLog);

        AppCompatButton login = findViewById(R.id.loginButton);

        login.setOnClickListener(view -> {
            if(email.getEditText().getText().toString().equals("")
                    || password.getEditText().getText().toString().equals("")){
                email.setError("All fields must not empty");
                password.setError("All fields must not empty");
            }else{
                email.setError(null);
                password.setError(null);

                String fetchEmail = email.getEditText().getText().toString();
                String fetchPassword = password.getEditText().getText().toString();

                reference.orderByChild("email").equalTo(fetchEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    email.setError(null);
                                    reference.orderByChild("password").equalTo(fetchPassword)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        password.setError(null);
                                                        loginUser();
                                                    }else{
                                                        password.setError("Wrong Password!");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }else{
                                    email.setError("Email does not exist.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
    }

    private void loginUser() {
        try {
            String userEmailLog = email.getEditText().getText().toString();
            String userPassLog = password.getEditText().getText().toString();

            ProgressBar progressBar = findViewById(R.id.loading);
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(userEmailLog, userPassLog).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    String userId = firebaseUser.getUid();
                    System.out.println(userId);
                    reference = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User");
                    Query user = reference.orderByChild("id").equalTo(userId);
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                DataSnapshot userSnapshot = snapshot.child(userId);
                                String[] keys = {"email", "location", "phoneNumber", "userName", "profilePicture", "coin", "id", "password"};

                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);

                                for (String key : keys)
                                    intent.putExtra(key, userSnapshot.child(key).getValue(String.class));

                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            });

        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(), getString(R.string.youve_left_a_field_empty), Toast.LENGTH_SHORT).show();
        }
    }

    public void openRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}