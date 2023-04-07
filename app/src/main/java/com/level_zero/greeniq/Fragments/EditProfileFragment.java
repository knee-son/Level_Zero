package com.level_zero.greeniq.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.level_zero.greeniq.Profile;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentEditProfileBinding;


import java.io.IOException;
import java.util.UUID;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private TextInputLayout userName, phone, location, password;
    private TextView email;
    private Uri imagePath;
    private ImageView avatar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        userName = binding.editUsername;
        password = binding.editPassword;
        phone = binding.editPhone;
        location = binding.editLocation;
        email = binding.emailProfile;

        avatar = binding.editAvatar;

        Bundle bundle = getActivity().getIntent().getExtras();
        String userEmail = bundle.getString("email");
        String userUserName = bundle.getString("userName");
        String userPhone = bundle.getString("phoneNumber");
        String userLocation = bundle.getString("location");
        String userAvatar = bundle.getString("profilePicture");
        String userCoin = bundle.getString("coin");
        String userPassword = bundle.getString("password");
        String userId = bundle.getString("id");

        email.setText(userEmail);
        userName.getEditText().setText(userUserName);
        phone.getEditText().setText(userPhone);
        location.getEditText().setText(userLocation);
        password.getEditText().setText(userPassword);

        Glide.with(this).load(userAvatar).error(R.drawable.defaultpfp).placeholder(R.drawable.defaultpfp).into(avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent pictureIntent = new Intent(Intent.ACTION_PICK);
//                pictureIntent.setType("image/*");
//                startActivityForResult(pictureIntent, 1);

                Intent pictureIntent = new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                getResult.launch(pictureIntent);
            }
        });

        binding.saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fetchUserName = userName.getEditText().getText().toString();
                String fetchPhone = phone.getEditText().getText().toString();
                String fetchLocation = location.getEditText().getText().toString();
                String fetchPassword = password.getEditText().getText().toString();

                Profile profile = new Profile(fetchUserName, fetchPhone, userAvatar, fetchLocation, userEmail, fetchPassword, userId, userCoin);
                databaseReference.child(userId).setValue(profile);
            }
        });

        binding.saveAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        return binding.getRoot();
    }

    private void saveImage() {
        ProgressDialog progressDialog = ProgressDialog.show(
                getActivity(),
                "Saving image...",
                "Please wait...",
                true);

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
                            Toast.makeText(getActivity(),"Saved Image", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(),"Failed to Save Image", Toast.LENGTH_SHORT).show();
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

    ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult r) {
                    if (r.getResultCode() == RESULT_OK && r.getData() != null) {
                        imagePath = r.getData().getData();
                        getImage();
                        System.out.println(imagePath);
                    }
                }
            }
    );

    private void getImage(){
        Bitmap bitmap = null;
        try{
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}