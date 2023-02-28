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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.WasteManagement.WasteScheduleActivity;
import com.level_zero.greeniq.databinding.FragmentWasteManagementBinding;

import com.google.android.gms.maps.SupportMapFragment;

public class WasteManagementFragment extends Fragment {

    private FragmentWasteManagementBinding binding;
    private ImageView wasteSchedule;
    private GoogleMap mMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWasteManagementBinding.inflate(inflater, container, false);

//        adding one more cardview:
//        binding.griddy.addView(new CardView(getActivity()));

       binding.griddy.getChildAt(0).setOnClickListener(view ->
                NavHostFragment.findNavController(WasteManagementFragment.this)
                .navigate(R.id.action_navigation_waste_management_to_wasteScheduleFragment));

       SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       if (mapFragment == null) {
           mapFragment = SupportMapFragment.newInstance();
           getChildFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
       }

       mapFragment.getMapAsync(new OnMapReadyCallback() {
           @Override
           public void onMapReady(GoogleMap googleMap) {
               mMap = googleMap;
               // Add your map customization and marker placement code here
           }
       });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}