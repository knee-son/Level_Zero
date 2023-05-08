package com.level_zero.greeniq;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Objects;

public class LoginActivity extends AppCompat {

    private TextInputLayout email, password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.userEmailLog);
        password = findViewById(R.id.userPasswordLog);

        AppCompatButton login = findViewById(R.id.loginButton);

        //login.setOnClickListener(view -> loginUser());

        login.setOnClickListener(view -> {
            if(!validateEmail()){
                email.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                email.setError(null);
                email.setErrorEnabled(false);
                loginUser();
            }
            if(!validatePassword()){
                password.setError("Field must have information");
                Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
            }else{
                password.setError(null);
                password.setErrorEnabled(false);
                loginUser();
            }
        });
    }

    private void loginUser() {
        try {
            String userEmailLog = RegisterActivity.safeFetch(email);
            String userPassLog = RegisterActivity.safeFetch(password);

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
                                String emailDB = snapshot.child(userId).child("email").getValue(String.class);
                                String locationDB = snapshot.child(userId).child("location").getValue(String.class);
                                String phoneNumberDB = snapshot.child(userId).child("phoneNumber").getValue(String.class);
                                String userNameDB = snapshot.child(userId).child("userName").getValue(String.class);
                                String profileDB = snapshot.child(userId).child("profilePicture").getValue(String.class);
                                String coinDB = snapshot.child(userId).child("coin").getValue(String.class);
                                String userDB = snapshot.child(userId).child("id").getValue(String.class);
                                String passwordDB = snapshot.child(userId).child("password").getValue(String.class);

                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("email", emailDB);
                                bundle.putString("location", locationDB);
                                bundle.putString("phoneNumber", phoneNumberDB);
                                bundle.putString("userName", userNameDB);
                                bundle.putString("profilePicture", profileDB);
                                bundle.putString("coin", coinDB);
                                bundle.putString("id", userDB);
                                bundle.putString("password", passwordDB);

//                                for (String s : new String[]{emailDB, locationDB, phoneNumberDB, userNameDB, profileDB})
//                                    System.out.println(s);

//                                HomeFragment fragobj = new HomeFragment();
//                                fragobj.setArguments(bundle);

                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(getApplicationContext(),"Login success!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Login failed!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
            });

        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(),"You've left a field empty!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        return !val.isEmpty();
    }//end of the method

    //validate user name or email
    private boolean validateEmail(){
        String val = Objects.requireNonNull(email.getEditText()).getText().toString();

        return !val.isEmpty();
    }
}