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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFootprintBinding;

import java.util.ArrayList;
import java.util.List;

public class CarbonFootprintFragment extends Fragment {

    private FragmentCarbonFootprintBinding binding;
    private Spinner choiceSpinner;
    private Spinner typeSpinner;
    private Button calculateButton;
    private PieChart pieChart;
    double footprintTransport = 0.0;
    double footprintFood = 0.0;
    double footprintElectricity = 0.0;

    private EditText amountEditText;
    String choiceValue, typeValue;
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
            }
        });

        return binding.getRoot();
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

        pieChart.setData(pieData);
        pieChart.invalidate();

        double total = footprintTransport + footprintElectricity + footprintFood;
        // Display the result to the user
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
