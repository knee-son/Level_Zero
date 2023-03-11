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
import android.widget.Toast;

import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFootprintBinding;

public class CarbonFootprintFragment extends Fragment {

    private FragmentCarbonFootprintBinding binding;
    private Spinner choiceSpinner;
    private Spinner typeSpinner;
    private Button calculateButton;

    private EditText amountEditText;
    String choiceValue, typeValue;
    String[] type = {"Transport", "Food","Electricity" };
    String[] transportType = {"Private", "Public", "Motorcycle"};
    String[] foodType = {"Pork", "Poultry", "Beef", "Fish", "Vegetables"};
    String[] electricityType= {"Low Usage","Medium Usage","High Usage"};


    private void calculateCarbonFootprint() {
        double carbonFootprint = 0.0;
        double amount = Double.parseDouble(amountEditText.getText().toString());

        if (choiceValue.equals("Transport")) {
            if (typeValue.equals("Private")) {
                carbonFootprint = amount * 2.5;
            } else if (typeValue.equals("Public")) {
                carbonFootprint = amount * 1.0;
            } else if (typeValue.equals("Motorcycle")) {
                carbonFootprint = amount * 1.2;
            }
        } else if (choiceValue.equals("Food")) {
            if (typeValue.equals("Pork")) {
                carbonFootprint = amount * 3.5;
            } else if (typeValue.equals("Poultry")) {
                carbonFootprint = amount * 2.5;
            } else if (typeValue.equals("Beef")) {
                carbonFootprint = amount * 7.0;
            } else if (typeValue.equals("Fish")) {
                carbonFootprint = amount * 1.8;
            } else if (typeValue.equals("Vegetables")) {
                carbonFootprint = amount * 0.5;
            }
        } else if (choiceValue.equals("Electricity")) {
            if (typeValue.equals("Low Usage")) {
                carbonFootprint = amount * 0.5;
            } else if (typeValue.equals("Medium Usage")) {
                carbonFootprint = amount * 1.0;
            } else if (typeValue.equals("High Usage")) {
                carbonFootprint = amount * 1.5;
            }
        }

        // Display the result to the user
        String result = String.format("Your carbon footprint is %.2f kg CO2e.", carbonFootprint);
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFootprintBinding.inflate(inflater, container, false);
        choiceSpinner = binding.choiceSpinner;
        typeSpinner = binding.typeSpinner;
        calculateButton =binding.calculateButton;
        amountEditText = binding.amountValue;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
