package com.level_zero.greeniq.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.level_zero.greeniq.R;
import com.level_zero.greeniq.WasteManagement.WasteScheduleActivity;
import com.level_zero.greeniq.databinding.FragmentWasteManagementBinding;

public class WasteManagementFragment extends Fragment {

    private FragmentWasteManagementBinding binding;
    private ImageView wasteSchedule;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWasteManagementBinding.inflate(inflater, container, false);

//        adding one more cardview:
//        binding.griddy.addView(new CardView(getActivity()));

       /* binding.griddy.getChildAt(0).setOnClickListener(view ->
                NavHostFragment.findNavController(WasteManagementFragment.this)
                .navigate(R.id.action_navigation_waste_management_to_wasteScheduleFragment));*/

        return binding.getRoot();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*public void openSchedule(View view) {
        NavHostFragment.findNavController(WasteManagementFragment.this).navigate(R.id.action_navigation_waste_management_to_wasteScheduleFragment);
    }*/
}