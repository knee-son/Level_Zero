package com.level_zero.greeniq.Fragments;

import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
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
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private LanguageManager languageManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentHomeBinding binding;
    private ImageSlider imageSlider;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReferenceCertificate;
    private FirebaseAuth firebaseAuth;

    private List<SlideModel> slideModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("User");
        databaseReferenceCertificate = firebaseDatabase.getReference("Certificate");

        firebaseAuth = FirebaseAuth.getInstance();

        TextView email = binding.emailTV;
        TextView username = binding.userNameTV;
        TextView phone = binding.phoneNumberTV;
        TextView location = binding.locationTV;
        TextView coin = binding.coinTV;
        TextView greeniq = binding.greeniqUser;

        ImageView avatar = binding.avatarIV;
        ImageView settings = binding.settings;

        imageSlider = binding.imageSlider;

        Bundle bundle = getActivity().getIntent().getExtras();
        String userEmail = bundle.getString("email");
        String userUserName = bundle.getString("userName");
        String userPhone = bundle.getString("phoneNumber");
        String userLocation = bundle.getString("location");
        String userAvatar = bundle.getString("profilePicture");
        String userCoin = bundle.getString("coin") + " coins";

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);
        coin.setText(userCoin);
        greeniq.setText("GreenIQ - "+userUserName);

        slideModels = new ArrayList<>();
        retrieveDataAndPopulateList();

//        ObjectAnimator slideLeft = ObjectAnimator.ofFloat(myImage, "translationX", 0f, -myImage.getWidth());
//        slideLeft.setDuration(1000);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(slideLeft);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_homeFragment_to_settingsFragment2);
            }
        });

        Glide.with(this).load(userAvatar).error(R.drawable.defaultpfp).placeholder(R.drawable.defaultpfp).into(avatar);

        return binding.getRoot();
    }

    private void retrieveDataAndPopulateList() {
        Bundle bundle = getActivity().getIntent().getExtras();
        String userUserName = bundle.getString("userName");

        databaseReferenceCertificate.child(userUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String url = dataSnapshot.child("z0").getValue(String.class);

                    slideModels.add(new SlideModel(url, null, ScaleTypes.CENTER_CROP));
                    imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

//// Get references to your views
//        View bindingProfile = findViewById(R.id.binding_profile);
//        View bindingCertificate = findViewById(R.id.binding_certificate);
//        View bindingCalendar = findViewById(R.id.binding_calendar);

// Set initial positions of views
        binding.profile.setTranslationX(binding.profile.getWidth());
        binding.certificate.setTranslationX(binding.profile.getWidth());
        binding.calendar.setTranslationX(binding.profile.getWidth());

// Create animations for each view
        ObjectAnimator titleAnimation = ObjectAnimator.ofFloat(binding.title, "alpha", 1);
        titleAnimation.setDuration(500);
        titleAnimation.setStartDelay(400);

        ObjectAnimator profileAnimation = ObjectAnimator.ofFloat(binding.profile, "translationX", 0);
        profileAnimation.setDuration(600);

        ObjectAnimator certificateAnimation = ObjectAnimator.ofFloat(binding.certificate, "translationX", 0);
        certificateAnimation.setDuration(600);
        certificateAnimation.setStartDelay(100);

        ObjectAnimator calendarAnimation = ObjectAnimator.ofFloat(binding.calendar, "translationX", 0);
        calendarAnimation.setDuration(600);
        calendarAnimation.setStartDelay(200);

// Start the animations
        titleAnimation.start();
        profileAnimation.start();
        certificateAnimation.start();
        calendarAnimation.start();
    }
}