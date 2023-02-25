package com.level_zero.greeniq.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.level_zero.greeniq.LoginActivity;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private TextView email, username, phone, location;
    private ImageView avatar;
    private AppCompatButton logout, save;
    private Uri imagePath;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");

        firebaseAuth = FirebaseAuth.getInstance();

        email = binding.emailTV;
        username = binding.userNameTV;
        phone = binding.phoneNumberTV;
        location = binding.locationTV;
        avatar = binding.avatarIV;
        logout = binding.logoutButton;
        save = binding.saveButton;

        Bundle bundle = getActivity().getIntent().getExtras();
        String userEmail = bundle.getString("email");
        String userUserName = bundle.getString("userName");
        String userPhone = bundle.getString("phoneNumber");
        String userLocation = bundle.getString("location");
        String userAvatar = bundle.getString("profilePicture");

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);

        Glide.with(this).load(userAvatar).error(R.drawable.defaultpfp).placeholder(R.drawable.defaultpfp).into(avatar);

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(),LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        avatar.setOnClickListener(view -> {
            Intent pictureIntent = new Intent(Intent.ACTION_PICK);
            pictureIntent.setType("image/*");
            getResult.launch(pictureIntent);
        });
        save.setOnClickListener(view -> saveImage());

        return binding.getRoot();
    }

    private void saveImage() {
        ProgressDialog progressDialog = ProgressDialog.show(
            getActivity(),"Saving image...","Please wait...",true);

        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString())
            .putFile(imagePath).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(t -> {
                        if(t.isSuccessful()){savedImage(t.getResult().toString());}
                    });
                    Toast.makeText(getActivity(),"Saved Image", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(),"Failed to Save Image", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            })
            .addOnProgressListener(snapshot -> {
                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage(" Uploading " +(int)progress+"%");
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
        }catch (IOException e){e.printStackTrace();}
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}