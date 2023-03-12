package com.level_zero.greeniq.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    private Spinner choiceSpinner;
    private Spinner typeSpinner;
    private Button calculateButton;
    private PieChart pieChart;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ListView listView;

    private EditText amountEditText;
    private ArrayList<String> dataList;
    String choiceValue, typeValue, currentUser;
    double footprintTransport = 0.0;
    double footprintFood = 0.0;
    double footprintElectricity = 0.0;
    String[] type = {"Transport", "Food","Electricity" };
    String[] transportType = {"Private", "Public", "Motorcycle"};
    String[] foodType = {"Pork", "Poultry", "Beef", "Fish", "Vegetables"};
    String[] electricityType= {"Low Usage","Medium Usage","High Usage"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFootprintBinding.inflate(inflater, container, false);
        choiceSpinner = binding.choiceSpinner;
        typeSpinner = binding.typeSpinner;
        calculateButton =binding.calculateButton;
        amountEditText = binding.amountValue;
        pieChart = binding.piechart;
        listView = binding.listviewHistory;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon History");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        choiceSpinner.setAdapter(adapter);
        choiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                choiceValue = value;

                // set the adapter of typeSpinner based on the selected value of choiceSpinner
                if(choiceValue.equals("Transport")){
                    ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, transportType);
                    typeSpinner.setAdapter(transportAdapter);
                } else if (choiceValue.equals("Food")) {
                    ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, foodType);
                    typeSpinner.setAdapter(foodAdapter);
                } else if (choiceValue.equals("Electricity")) {
                    ArrayAdapter<String> electricityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, electricityType);
                    typeSpinner.setAdapter(electricityAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                typeValue = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCarbonFootprint();
                addHistory();
            }
        });

        displayDataHistory();

        return binding.getRoot();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, dataList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calculateCarbonFootprint() {
        double amount = Double.parseDouble(amountEditText.getText().toString());

        if (choiceValue.equals("Transport")) {
            if (typeValue.equals("Private")) {
                footprintTransport = amount * 5.8/100 * 2.3035;
            } else if (typeValue.equals("Public")) {
                footprintTransport = amount * 18.181818182/100 * 2.3035;
            } else if (typeValue.equals("Motorcycle")) {
                footprintTransport = amount * 1.8867924528/100 * 2.3035;
            }
        } else if (choiceValue.equals("Food")) {
            if (typeValue.equals("Pork")) {
                footprintFood = amount * 7.6;
            } else if (typeValue.equals("Poultry")) {
                footprintFood = amount * 6.9;
            } else if (typeValue.equals("Beef")) {
                footprintFood = amount * 27;
            } else if (typeValue.equals("Fish")) {
                footprintFood = amount * 11;
            } else if (typeValue.equals("Vegetables")) {
                footprintFood = amount * 2;
            }
        } else{
            if (typeValue.equals("Low Usage")) {
                footprintElectricity = amount * 1.35;
            } else if (typeValue.equals("Medium Usage")) {
                footprintElectricity = amount * 5;
            } else if (typeValue.equals("High Usage")) {
                footprintElectricity = amount * 17;
            }
        }

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) footprintTransport, "Transport"));
        entries.add(new PieEntry((float) footprintFood, "Food"));
        entries.add(new PieEntry((float) footprintElectricity, "Electricity"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Carbon");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData pieData = new PieData(pieDataSet);

        pieChart.getDescription().setEnabled(false);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void addHistory() {
        String currentDate = simpleDateFormat.format(calendar.getTime());
        double total = footprintTransport + footprintElectricity + footprintFood;

        History history = new History(currentDate, String.valueOf(total));
        databaseReference.child(currentUser).child("testDate").setValue(history);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
