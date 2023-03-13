package com.level_zero.greeniq.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.History;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFoodBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarbonFoodFragment extends Fragment {

    private FragmentCarbonFoodBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner typeSpinner;
    private EditText amountEditText;
    private Button button;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    private String typeValue, currentUser;
    double footprintFood;

    private final String[] foodType = {"Pork", "Poultry", "Beef", "Fish", "Vegetables"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFoodBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinnerFood;
        amountEditText = binding.amountValueFood;
        button = binding.calculateButtonFood;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, foodType);
        typeSpinner.setAdapter(foodAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueDatabase();
                addtoHistory();
            }
        });

        return binding.getRoot();
    }

    private void addtoHistory() {
        databaseReference.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String transportData = snapshot.child("transportTotal").getValue(String.class);
                String foodData = snapshot.child("foodTotal").getValue(String.class);
                String electricityData = snapshot.child("electricityTotal").getValue(String.class);

                double valueTransport = Double.valueOf(transportData);
                double valueFood = Double.valueOf(foodData);
                double valueElectricity = Double.valueOf(electricityData);

                double total = valueTransport + valueFood + valueElectricity;
                String currentDate = simpleDateFormat.format(calendar.getTime());

                History history = new History(currentDate, String.valueOf(total));

                firebaseDatabase.getReference("Carbon History").child(currentUser).child(currentDate).setValue(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void valueDatabase() {
        double amount = Double.parseDouble(amountEditText.getText().toString());

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

        databaseReference.child(currentUser).child("foodTotal").setValue(String.valueOf(footprintFood));
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}