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
import com.level_zero.greeniq.databinding.FragmentCarbonElectricityBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarbonElectricityFragment extends Fragment {

    private FragmentCarbonElectricityBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner typeSpinner;
    private Button button;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    String typeValue, currentUser;
    double footprintElectricity;

    String[] electricityType = {"Low Usage", "Medium Usage", "High Usage"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonElectricityBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinnerElectricity;
        button = binding.calculateButtonElectricity;

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        ArrayAdapter<String> electricityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, electricityType);
        typeSpinner.setAdapter(electricityAdapter);

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
        if (typeValue.equals("Low Usage")) {
            footprintElectricity = 24 * 1.35;
        } else if (typeValue.equals("Medium Usage")) {
            footprintElectricity = 24 * 5;
        } else if (typeValue.equals("High Usage")) {
            footprintElectricity = 24 * 17;
        }

        databaseReference.child(currentUser).child("electricityTotal").setValue(String.valueOf(footprintElectricity));
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}