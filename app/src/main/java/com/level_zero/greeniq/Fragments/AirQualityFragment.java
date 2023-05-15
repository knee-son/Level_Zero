package com.level_zero.greeniq.Fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
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
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentAirQualityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

public class AirQualityFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentAirQualityBinding binding;
    private TextView AQI, nearestCityTv;
    BarData barData;
    BarChart barChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAirQualityBinding.inflate(inflater, container, false);

        AQI = binding.AQItv;
        nearestCityTv = binding.nearestCityTv;
        barChart = binding.chart;

        getChart();

        Button button = binding.button4;
        button.setOnClickListener(this::get);

        return binding.getRoot();
    }

    private void get(View v) {
        String apikey="28b92be4-41a2-4905-983f-280e20cef31a";
        String url="https://api.airvisual.com/v2/nearest_city?key="+apikey;
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject data = response.getJSONObject("data");
                String city = data.getString("city");
                nearestCityTv.setText(city);

                JSONObject current = data.getJSONObject("current");
                JSONObject pollution = current.getJSONObject("pollution");

                int aqi_us = pollution.getInt("aqius");
                String[] levels = {"Good", "Moderate", "Alarming", "Unhealthy", "Very Unhealthy", "Hazardous"};
                int levelIndex = Math.min(aqi_us / 50, levels.length - 1);
                String level = aqi_us + " - " + levels[levelIndex];

                AQI.setText(level);

                set(String.valueOf(aqi_us));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show());
        queue.add(request);
    }
    private void set(String level) {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        firebaseDatabase.getReference("History").child(daysOfWeek[dayOfWeek]).setValue(level);
    }

    private void getChart() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("History");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TypedArray a = requireContext().getTheme().obtainStyledAttributes(R.style.TEXT, new int[]{android.R.attr.textColor});
                int color = a.getColor(0, 0);
                a.recycle();

                int barColor = ContextCompat.getColor(requireContext(), R.color.my_bar_color);

                // Create an array of day names and an array of values
                String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                int[] values = new int[7];
                for (int i = 0; i < daysOfWeek.length; i++) {
                    values[i] = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child(daysOfWeek[i]).getValue()).toString());
                }

                // Create an array of BarEntry objects
                BarEntry[] entries = new BarEntry[7];
                for (int i = 0; i < entries.length; i++) {
                    entries[i] = new BarEntry(i, values[i]);
                }

                // Create an array of BarDataSet objects
                BarDataSet[] dataSets = new BarDataSet[7];
                for (int i = 0; i < dataSets.length; i++) {
                    dataSets[i] = new BarDataSet(Collections.singletonList(entries[i]), daysOfWeek[i]);
                    dataSets[i].setColor(barColor);
                    dataSets[i].setValueTextColor(color);
                    dataSets[i].setValueTextSize(12f);
                }

                // Create a BarData object that contains all of the BarDataSets
                barData = new BarData(dataSets);
                barData.setBarWidth(0.9f);

                // Customize the appearance of the chart
                barChart.setBorderColor(color);
                barChart.setData(barData);
                barChart.getDescription().setEnabled(false);
                barChart.setDrawGridBackground(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisLeft().setTextColor(color);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisRight().setTextColor(color);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getXAxis().setTextColor(color);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList(daysOfWeek)));
                barChart.getLegend().setEnabled(false);
                barChart.getBarData().setValueTextColor(color);

                barChart.animateY(400, Easing.EaseOutQuart);
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}