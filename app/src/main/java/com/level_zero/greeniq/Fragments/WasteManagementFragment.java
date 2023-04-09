package com.level_zero.greeniq.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentWasteManagementBinding;

import com.google.android.gms.maps.SupportMapFragment;

public class WasteManagementFragment extends Fragment {

    private LanguageManager languageManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
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

       binding.griddy1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               NavHostFragment.findNavController(WasteManagementFragment.this).navigate(R.id.action_wasteManagementFragment_to_wasteEventFragment);
           }
       });

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
               LatLng cebu = new LatLng(10.2954233,123.9017223);
               LatLng talisay = new LatLng(10.2732741,123.857236);
               LatLng lapulapu = new LatLng(10.2921273,123.9591187);
               mMap.addMarker(new MarkerOptions()
                       .position(cebu)
                       .title("Waste Center in Cebu City"));
               mMap.addMarker(new MarkerOptions()
                       .position(talisay)
                       .title("Waste Center in Talisay City"));
               mMap.addMarker(new MarkerOptions()
                       .position(lapulapu)
                       .title("Waste Center in LapuLapu City"));
               mMap.moveCamera(CameraUpdateFactory.newLatLng(cebu));
               mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.0f ) );
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