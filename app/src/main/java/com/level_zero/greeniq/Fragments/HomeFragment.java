package com.level_zero.greeniq.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.level_zero.greeniq.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        String userEmail = getArguments().getString("email");
        String userUserName = getArguments().getString("userName");
        String userPhone = getArguments().getString("phoneNumber");
        String userLocation = getArguments().getString("location");
        String userAvatar = getArguments().getString("profilePicture");
//
//        System.out.println(userEmail);
//        System.out.println(userUserName);
//        System.out.println(userPhone);
//        System.out.println(userLocation);
//        System.out.println(userAvatar);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}