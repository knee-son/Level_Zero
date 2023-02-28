package com.level_zero.greeniq.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentAirQualityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AirQualityFragment extends Fragment {

    private FragmentAirQualityBinding binding;
    private TextView AQI, nearestCityTv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAirQualityBinding.inflate(inflater, container, false);

        AQI = binding.AQItv;
        nearestCityTv = binding.nearestCityTv;

        getChart();

        Button button = binding.button4;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get(v);
            }
        });

        return binding.getRoot();
    }

    private void get(View v) {
        String apikey="28b92be4-41a2-4905-983f-280e20cef31a";
        String url="https://api.airvisual.com/v2/nearest_city?key=28b92be4-41a2-4905-983f-280e20cef31a";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String city = data.getString("city");
                    nearestCityTv.setText(city);

                    JSONObject current = data.getJSONObject("current");
                    JSONObject pollution = current.getJSONObject("pollution");
                    int aqius = pollution.getInt("aqius");
                    AQI.setText(String.valueOf(aqius));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void getChart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("History");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int mondayValue = Integer.parseInt(dataSnapshot.child("monday").getValue().toString());
                int tuesdayValue = Integer.parseInt(dataSnapshot.child("tuesday").getValue().toString());
                int wednesdayValue = Integer.parseInt(dataSnapshot.child("wednesday").getValue().toString());
                int thursdayValue = Integer.parseInt(dataSnapshot.child("thursday").getValue().toString());
                int fridayValue = Integer.parseInt(dataSnapshot.child("friday").getValue().toString());
                int saturdayValue = Integer.parseInt(dataSnapshot.child("saturday").getValue().toString());
                int sundayValue = Integer.parseInt(dataSnapshot.child("sunday").getValue().toString());

                // Create a BarEntry object for each day of the week
                BarEntry mondayEntry = new BarEntry(0f, mondayValue);
                BarEntry tuesdayEntry = new BarEntry(1f, tuesdayValue);
                BarEntry wednesdayEntry = new BarEntry(2f, wednesdayValue);
                BarEntry thursdayEntry = new BarEntry(3f, thursdayValue);
                BarEntry fridayEntry = new BarEntry(4f, fridayValue);
                BarEntry saturdayEntry = new BarEntry(5f, saturdayValue);
                BarEntry sundayEntry = new BarEntry(6f, sundayValue);

                // Create a BarDataSet object for each day of the week
                BarDataSet mondayDataSet = new BarDataSet(Arrays.asList(mondayEntry), "Monday");
                BarDataSet tuesdayDataSet = new BarDataSet(Arrays.asList(tuesdayEntry), "Tuesday");
                BarDataSet wednesdayDataSet = new BarDataSet(Arrays.asList(wednesdayEntry), "Wednesday");
                BarDataSet thursdayDataSet = new BarDataSet(Arrays.asList(thursdayEntry), "Thursday");
                BarDataSet fridayDataSet = new BarDataSet(Arrays.asList(fridayEntry), "Friday");
                BarDataSet saturdayDataSet = new BarDataSet(Arrays.asList(saturdayEntry), "Saturday");
                BarDataSet sundayDataSet = new BarDataSet(Arrays.asList(sundayEntry), "Sunday");

                // Customize the appearance of the BarDataSets
                mondayDataSet.setColor(Color.GREEN);
                mondayDataSet.setValueTextColor(Color.BLACK);
                mondayDataSet.setValueTextSize(12f);

                tuesdayDataSet.setColor(Color.GREEN);
                tuesdayDataSet.setValueTextColor(Color.BLACK);
                tuesdayDataSet.setValueTextSize(12f);

                wednesdayDataSet.setColor(Color.GREEN);
                wednesdayDataSet.setValueTextColor(Color.BLACK);
                wednesdayDataSet.setValueTextSize(12f);

                thursdayDataSet.setColor(Color.GREEN);
                thursdayDataSet.setValueTextColor(Color.BLACK);
                thursdayDataSet.setValueTextSize(12f);

                fridayDataSet.setColor(Color.GREEN);
                fridayDataSet.setValueTextColor(Color.BLACK);
                fridayDataSet.setValueTextSize(12f);

                saturdayDataSet.setColor(Color.GREEN);
                saturdayDataSet.setValueTextColor(Color.BLACK);
                saturdayDataSet.setValueTextSize(12f);

                sundayDataSet.setColor(Color.GREEN);
                sundayDataSet.setValueTextColor(Color.BLACK);
                sundayDataSet.setValueTextSize(12f);

                // Create a BarData object that contains all of the BarDataSets
                BarData barData = new BarData(mondayDataSet, tuesdayDataSet, wednesdayDataSet,
                        thursdayDataSet, fridayDataSet, saturdayDataSet, sundayDataSet);
                barData.setBarWidth(0.9f);

                // Customize the appearance of the chart
                BarChart barChart = binding.chart;
                barChart.setData(barData);
                barChart.getDescription().setEnabled(false); // Disable the description label
                barChart.setDrawGridBackground(false); // Disable the background grid lines
                barChart.getAxisLeft().setDrawGridLines(false); // Disable the left y-axis grid lines
                barChart.getAxisRight().setDrawGridLines(false); // Disable the right y-axis grid lines
                barChart.getXAxis().setDrawGridLines(false); // Disable the x-axis grid lines
                barChart.getXAxis().setGranularity(1f); // Set the x-axis granularity to 1 (one label per day of the week)
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // Set the position of the x-axis to the bottom
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))); // Set the x-axis labels to the abbreviated day names
                barChart.getLegend().setEnabled(false); // Disable the legend

                // Refresh the chart
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value." + error.toException());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}