package com.level_zero.greeniq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.userEmailLog);
        password = findViewById(R.id.userPasswordLog);
        AppCompatButton login = findViewById(R.id.loginButton);

        login.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        try {
            String userEmailLog = RegisterActivity.safeFetch(email);
            String userPassLog = RegisterActivity.safeFetch(password);

            firebaseAuth.signInWithEmailAndPassword(userEmailLog, userPassLog).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    String userId = firebaseUser.getUid();
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

                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);

                                intent.putExtra("email", emailDB);
                                intent.putExtra("location", locationDB);
                                intent.putExtra("phoneNumber", phoneNumberDB);
                                intent.putExtra("userName", userNameDB);
                                intent.putExtra("profilePicture", profileDB);

                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(getApplicationContext(),"Login is Complete", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Login is not Complete", Toast.LENGTH_SHORT).show();
                }
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
}