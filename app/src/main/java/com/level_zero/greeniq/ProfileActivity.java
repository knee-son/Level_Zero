package com.level_zero.greeniq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private TextView email, username, phone, location;
    private ImageView avatar;
    private AppCompatButton logout, save, dashboard;
    private Uri imagePath;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailTV);
        username = findViewById(R.id.userNameTV);
        phone = findViewById(R.id.phoneNumberTV);
        location = findViewById(R.id.locationTV);

        avatar = findViewById(R.id.avatarIV);

        logout = findViewById(R.id.logoutButton);
        save = findViewById(R.id.saveButton);
        dashboard = findViewById(R.id.dashboardButton);

        Intent intent = getIntent();

        String userEmail = intent.getStringExtra("email");
        String userUserName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("phoneNumber");
        String userLocation = intent.getStringExtra("location");
        String userAvatar = intent.getStringExtra("profilePicture");

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);

        Glide.with(this).load(userAvatar).error(R.drawable.defaultpfp).placeholder(R.drawable.defaultpfp).into(avatar);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent, 1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving Image.....");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString())
                .putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        savedImage(task.getResult().toString());
                                    }
                                }
                            });
                            Toast.makeText(getApplicationContext(),"Saved Image", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"Failed to Save Image", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage(" Uploading " +(int)progress+"%");

                    }
                });
    }

    private void savedImage(String url) {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()+"/profilePicture").setValue(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imagePath = data.getData();
            getImage();
        }
    }

    private void getImage(){
        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        avatar.setImageBitmap(bitmap);
    }
}