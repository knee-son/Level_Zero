package com.level_zero.greeniq.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.History;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFootprintBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarbonFootprintFragment extends Fragment {

    private FragmentCarbonFootprintBinding binding;
    private PieChart pieChart;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, databaseReferenceData;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ListView listView;
    private CardView cardView1, cardView2, cardView3;
    private ArrayList<String> dataList;
    String currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFootprintBinding.inflate(inflater, container, false);
        pieChart = binding.piechart;
        listView = binding.listviewHistory;
        cardView1 = binding.cardView1;
        cardView2 = binding.cardView2;
        cardView3 = binding.cardView3;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon History");
        databaseReferenceData = firebaseDatabase.getReference("Carbon Data");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        displayPieChart();
        displayDataHistory();

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarbonFootprintFragment.this).navigate(R.id.action_carbonFootprintFragment_to_carbonTransportFragment);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarbonFootprintFragment.this).navigate(R.id.action_carbonFootprintFragment_to_carbonFoodFragment);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarbonFootprintFragment.this).navigate(R.id.action_carbonFootprintFragment_to_carbonElectricityFragment);
            }
        });

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

                entries.add(new PieEntry(Float.parseFloat(transportData), "Transport"));
                entries.add(new PieEntry(Float.parseFloat(foodData), "Food"));
                entries.add(new PieEntry(Float.parseFloat(electricityData), "Electricity"));

                PieDataSet pieDataSet = new PieDataSet(entries, "Carbon");
                pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                PieData pieData = new PieData(pieDataSet);

                pieChart.getDescription().setEnabled(false);

                pieChart.setData(pieData);
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

                    // Add the data to the ArrayList
                    dataList.add(date + " - Total Carbon: " + totalCarbon);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
                listView.setAdapter(adapter);
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
