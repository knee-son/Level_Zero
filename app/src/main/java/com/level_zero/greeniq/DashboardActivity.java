package com.level_zero.greeniq;

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

        com.level_zero.greeniq.databinding.ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_waste_management, R.id.navigation_carbon_footprint, R.id.navigation_air_quality)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
//    }
//
//    public void openRecycling(View view) {
//        Intent intent = new Intent(getApplicationContext(), WasteManagementActivity.class);
//        startActivity(intent);
//    }
//
//    public void openAirQuality(View view) {
//        Intent intent = new Intent(getApplicationContext(), AirQualityActivity.class);
//        startActivity(intent);
//    }
//
//    public void openCarbon(View view) {
//        Intent intent = new Intent(getApplicationContext(), CarbonFootprintActivity.class);
//        startActivity(intent);
//    }
}