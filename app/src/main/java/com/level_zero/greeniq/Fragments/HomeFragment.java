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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private TextView email, username, phone, location, coin;
    private ImageView avatar, settings;
    private ImageSlider imageSlider;
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
        settings = binding.settings;
        coin = binding.coinTV;
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

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2F1.jpg?alt=media&token=70b0e0ba-9311-4d35-b595-74971d435b69", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2F2.jpg?alt=media&token=2d9d6c64-bc99-4a68-8852-6bb827d2477b", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/certificate%2F3.jpg?alt=media&token=7971af6a-1b37-413f-a662-7339b613f4b9", null, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

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
        profileAnimation.setDuration(300);

        ObjectAnimator certificateAnimation = ObjectAnimator.ofFloat(binding.certificate, "translationX", 0);
        certificateAnimation.setDuration(300);
        certificateAnimation.setStartDelay(100);

        ObjectAnimator calendarAnimation = ObjectAnimator.ofFloat(binding.calendar, "translationX", 0);
        calendarAnimation.setDuration(300);
        calendarAnimation.setStartDelay(200);

// Start the animations
        titleAnimation.start();
        profileAnimation.start();
        certificateAnimation.start();
        calendarAnimation.start();
    }
}