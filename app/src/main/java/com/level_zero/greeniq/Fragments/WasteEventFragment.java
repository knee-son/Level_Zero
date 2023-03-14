package com.level_zero.greeniq.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFoodBinding;
import com.level_zero.greeniq.databinding.FragmentWasteEventBinding;

public class WasteEventFragment extends Fragment {

    private FragmentWasteEventBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWasteEventBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}