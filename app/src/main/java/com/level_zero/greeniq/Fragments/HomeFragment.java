package com.level_zero.greeniq.Fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentHomeBinding binding;
    private ImageSlider imageSlider;
    private DatabaseReference databaseReferenceCertificate;
    private List<SlideModel> slideModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReferenceCertificate = firebaseDatabase.getReference("Certificate");
        firebaseDatabase.getReference("User");
        FirebaseAuth.getInstance();

        TextView email = binding.emailTV;
        TextView username = binding.userNameTV;
        TextView phone = binding.phoneNumberTV;
        TextView location = binding.locationTV;
        TextView coin = binding.coinTV;
        TextView greenIQ = binding.greeniqUser;

        ImageView avatar = binding.avatarIV;
        ImageView settings = binding.settings;
        imageSlider = binding.imageSlider;

        Bundle bundle = requireActivity().getIntent().getExtras();
        String userEmail = bundle.getString("email");
        String userUserName = bundle.getString("userName");
        String userPhone = bundle.getString("phoneNumber");
        String userLocation = bundle.getString("location");
        String userAvatar = bundle.getString("profilePicture");
        String userCoin = bundle.getString("coin")+" coins";
        String greenIQProfile = getString(R.string.greeniq_profile)+userUserName;

        email.setText(userEmail);
        username.setText(userUserName);
        phone.setText(userPhone);
        location.setText(userLocation);
        coin.setText(userCoin);
        greenIQ.setText(greenIQProfile);

        slideModels = new ArrayList<>();
        retrieveDataAndPopulateList();

        settings.setOnClickListener(view -> NavHostFragment
            .findNavController(HomeFragment.this)
            .navigate(R.id.action_homeFragment_to_settingsFragment2));

        Glide
            .with(this)
            .load(userAvatar)
            .error(R.drawable.defaultpfp)
            .placeholder(R.drawable.defaultpfp)
            .into(avatar);

        ScrollView scrollView = binding.scrollview;
        View imageView1 = binding.view1;
        View imageView2 = binding.view2;
        scrollView.setOnScrollChangeListener(
            (view, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int scrollDistance = view.getTop() - scrollY;
                imageView1.setTranslationY(scrollDistance*2.1f);
                imageView2.setTranslationY(scrollDistance*2.1f);
        });

        return binding.getRoot();
    }

    private void retrieveDataAndPopulateList() {
        Bundle bundle = requireActivity().getIntent().getExtras();
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

        binding.profile.setTranslationX(binding.profile.getWidth());
        binding.certificate.setTranslationX(binding.profile.getWidth());
        binding.calendar.setTranslationX(binding.profile.getWidth());

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

        titleAnimation.start();
        profileAnimation.start();
        certificateAnimation.start();
        calendarAnimation.start();
    }
}