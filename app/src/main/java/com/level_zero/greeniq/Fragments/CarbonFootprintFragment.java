package com.level_zero.greeniq.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFootprintBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CarbonFootprintFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentCarbonFootprintBinding binding;
    private ScrollView scrollView;
    private PieChart pieChart;
    private DatabaseReference databaseReference, databaseReferenceData;
    private ListView listView;
    private ArrayList<String> dataList;
    String currentUser;
    int currentNightMode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFootprintBinding.inflate(inflater, container, false);

        pieChart = binding.piechart;
        listView = binding.listviewHistory;
        CardView cardView1 = binding.cardView1;
        CardView cardView2 = binding.cardView2;
        CardView cardView3 = binding.cardView3;

        scrollView = binding.scrollView;

        currentNightMode = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        Bundle bundle = requireActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon History");
        databaseReferenceData = firebaseDatabase.getReference("Carbon Data");

//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        displayPieChart();
        displayDataHistory();

        cardView1.setOnClickListener(view -> NavHostFragment
            .findNavController(CarbonFootprintFragment.this)
            .navigate(R.id.action_carbonFootprintFragment_to_carbonTransportFragment));

        cardView2.setOnClickListener(view -> NavHostFragment
            .findNavController(CarbonFootprintFragment.this)
            .navigate(R.id.action_carbonFootprintFragment_to_carbonFoodFragment));

        cardView3.setOnClickListener(view -> NavHostFragment
            .findNavController(CarbonFootprintFragment.this)
            .navigate(R.id.action_carbonFootprintFragment_to_carbonElectricityFragment));

        return binding.getRoot();
    }

    private void displayPieChart() {
        databaseReferenceData.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String transportData = snapshot.child("transportTotal").getValue(String.class);
                String foodData = snapshot.child("foodTotal").getValue(String.class);
                String electricityData = snapshot.child("electricityTotal").getValue(String.class);

                List<PieEntry> entries = new ArrayList<>();

                entries.add(new PieEntry(Float.parseFloat(Objects.requireNonNull(transportData)), "Transport")  );
                entries.add(new PieEntry(Float.parseFloat(Objects.requireNonNull(foodData)), "Food"));
                entries.add(new PieEntry(Float.parseFloat(Objects.requireNonNull(electricityData)), "Electricity"));

                PieDataSet pieDataSet = new PieDataSet(entries, null);
                pieDataSet.setDrawValues(true);
                pieDataSet.setValueTextSize(12f);
                pieDataSet.setValueTextColor(Color.BLACK);
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
                        break;
                }

                PieData pieData = new PieData(pieDataSet);
                Legend legend = pieChart.getLegend();
                legend.setXEntrySpace(30);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                legend.setTextSize(15f);
                legend.setTextColor(Color.GRAY);
                legend.setYOffset(16f);

                pieChart.setCenterTextOffset(0,50f);
                pieChart.setTransparentCircleAlpha(0);
                pieChart.setHoleColor(Color.alpha(0));
                pieChart.getDescription().setEnabled(false);
                pieChart.setData(pieData);
                pieChart.animateY(1500, Easing.EaseOutBounce);
                pieChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayDataHistory() {
        dataList = new ArrayList<>();

        databaseReference.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Retrieve the date and totalCarbon values
                    String date = childSnapshot.child("date").getValue(String.class);
                    String totalCarbon = childSnapshot.child("totalCarbon").getValue(String.class);
                    float floatValue = Float.parseFloat(Objects.requireNonNull(totalCarbon));
                    String decimal = String.format(Locale.US, "%.2f", floatValue);
                    // Add the data to the ArrayList
                    dataList.add(date + " - Total Carbon: " + decimal);
//                    dataList.setTextColor(Color.RED);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);

                        switch (currentNightMode) {
                            case Configuration.UI_MODE_NIGHT_NO:
                                textView.setTextColor(Color.BLACK); // Set the text color for the light theme
                                break;
                            case Configuration.UI_MODE_NIGHT_YES:
                                textView.setTextColor(Color.WHITE); // Set the text color for the dark theme
                                break;
                        }
                        textView.setTextSize(14f);
                        textView.setAlpha(0.9f);
                        return view;
                    }
                };
                listView.setAdapter(adapter);
                scrollView.scrollTo(0, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
