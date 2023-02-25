package com.level_zero.greeniq;

import static androidx.navigation.Navigation.findNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.level_zero.greeniq.AirQuality.AirQualityActivity;
import com.level_zero.greeniq.CarbonFootprint.CarbonFootprintActivity;
import com.level_zero.greeniq.WasteManagement.WasteManagementActivity;
import com.level_zero.greeniq.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        for (String s : new String[]{"email", "userName", "phoneNumber", "location", "profilePicture"})
//            System.out.println(getIntent().getExtras().getString(s));

        com.level_zero.greeniq.databinding.ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}