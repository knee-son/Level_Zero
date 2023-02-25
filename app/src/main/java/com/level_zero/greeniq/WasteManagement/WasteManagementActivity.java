package com.level_zero.greeniq.WasteManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.level_zero.greeniq.R;

public class WasteManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste_management);
    }

//    public void openSchedule(View view) {
//        Intent intent = new Intent(getApplicationContext(), WasteScheduleActivity.class);
//        startActivity(intent);
//    }
}