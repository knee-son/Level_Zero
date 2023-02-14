package com.level_zero.greeniq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.level_zero.greeniq.AirQuality.AirQualityActivity;
import com.level_zero.greeniq.CarbonFootprint.CarbonFootprintActivity;
import com.level_zero.greeniq.WasteManagement.WasteManagementActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void openRecycling(View view) {
        Intent intent = new Intent(getApplicationContext(), WasteManagementActivity.class);
        startActivity(intent);
    }

    public void openAirQuality(View view) {
        Intent intent = new Intent(getApplicationContext(), AirQualityActivity.class);
        startActivity(intent);
    }

    public void openCarbon(View view) {
        Intent intent = new Intent(getApplicationContext(), CarbonFootprintActivity.class);
        startActivity(intent);
    }
}