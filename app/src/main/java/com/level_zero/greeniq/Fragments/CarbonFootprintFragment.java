package com.level_zero.greeniq.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFootprintBinding;

public class CarbonFootprintFragment extends Fragment {

    private FragmentCarbonFootprintBinding binding;
    private Spinner choiceSpinner, typeSpinner;
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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
