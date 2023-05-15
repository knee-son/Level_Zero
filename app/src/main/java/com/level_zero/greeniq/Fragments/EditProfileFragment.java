package com.level_zero.greeniq.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentEditProfileBinding;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class EditProfileFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentEditProfileBinding binding;
    private TextInputLayout userName, phone, location, password;
    private Uri imagePath;
    private ImageView avatar;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ImageView back;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        back = binding.back1;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userName = binding.editUsername;
        password = binding.editPassword;
        phone = binding.editPhone;
        location = binding.editLocation;
        TextView email = binding.emailProfile;

        avatar = binding.editAvatar;

        Bundle bundle = requireActivity().getIntent().getExtras();
        String userEmail = bundle.getString("email");
        String userUserName = bundle.getString("userName");
        String userPhone = bundle.getString("phoneNumber");
        String userLocation = bundle.getString("location");
        String userAvatar = bundle.getString("profilePicture");
        String userPassword = bundle.getString("password");
        String userId = bundle.getString("id");

        email.setText(userEmail);
        Objects.requireNonNull(userName.getEditText()).setText(userUserName);
        Objects.requireNonNull(phone.getEditText()).setText(userPhone);
        Objects.requireNonNull(location.getEditText()).setText(userLocation);
        Objects.requireNonNull(password.getEditText()).setText(userPassword);

        Glide.with(this).load(userAvatar).error(R.drawable.defaultpfp).placeholder(R.drawable.defaultpfp).into(avatar);

        avatar.setOnClickListener(view -> {
            Intent pictureIntent = new Intent(Intent.ACTION_PICK);
            pictureIntent.setType("image/*");
            getResult.launch(pictureIntent);
        });

        binding.saveProfile.setOnClickListener(view -> {
            String fetchUserName = userName.getEditText().getText().toString();
            String fetchPhone = phone.getEditText().getText().toString();
            String fetchLocation = location.getEditText().getText().toString();
            String fetchPassword = password.getEditText().getText().toString();

            databaseReference.orderByChild("userName").equalTo(fetchUserName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                userName.setError("Username exists.");
                            }else{
                                userName.setError(null);
                                if(fetchPassword.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$")){
                                    password.setError(null);
                                    if(fetchPhone.matches("^(09|\\+639)\\d{9}$")){
                                        phone.setError(null);

                                        firebaseUser.updatePassword(fetchPassword)
                                            .addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    Query query = databaseReference.orderByChild("id").equalTo(userId);
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                            for(DataSnapshot dataSnapshot : snapshot1.getChildren()){
                                                                dataSnapshot.getRef().child("userName").setValue(fetchUserName);
                                                                dataSnapshot.getRef().child("password").setValue(fetchPassword);
                                                                dataSnapshot.getRef().child("phoneNumber").setValue(fetchPhone);
                                                                dataSnapshot.getRef().child("location").setValue(fetchLocation);

                                                                Toast.makeText(getContext(), "Updating data of "+userEmail, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                    requireActivity().finish();

                                                    Intent intent =
                                                        requireActivity()
                                                        .getBaseContext()
                                                        .getPackageManager()
                                                        .getLaunchIntentForPackage(
                                                            requireActivity()
                                                            .getBaseContext()
                                                            .getPackageName());
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                }
                                            });
                                    }else{
                                        phone.setError("Phone number is invalid.");
                                    }
                                }else{
                                    password.setError("Should have one uppercase and lowercase character, one digit number, and one special character.");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });

        binding.saveAvatar.setOnClickListener(view -> saveImage());

        return binding.getRoot();
    }

    private void saveImage() {
        ProgressDialog progressDialog = ProgressDialog.show(
                getActivity(),
                "Saving image...",
                "Please wait...",
                true);

        FirebaseStorage.getInstance().getReference("images/"+ UUID.randomUUID().toString())
                .putFile(imagePath).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful())
                                savedImage(task1.getResult().toString());
                        });
                        Toast.makeText(getActivity(),"Saved Image", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(),"Failed to Save Image", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }).addOnProgressListener(snapshot -> {
                    double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                    progressDialog.setMessage(" Uploading " +(int)progress+"%");
                });
    }

    private void savedImage(String url) {
        Bundle bundle = requireActivity().getIntent().getExtras();
        String userId = bundle.getString("id");
        Query query = databaseReference.orderByChild("id").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().child("profilePicture").setValue(url);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setOnClickListener(view1 ->
            NavHostFragment
            .findNavController(EditProfileFragment.this)
            .navigate(R.id.action_editProfileFragment_to_settingsFragment));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}