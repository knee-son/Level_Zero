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
import com.level_zero.greeniq.databinding.FragmentCarbonTransportBinding;;import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarbonTransportFragment extends Fragment {

    private FragmentCarbonTransportBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private EditText amountEditText;
    private Button button;
    private Spinner typeSpinner;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    String typeValue, currentUser;
    double footprintValue;

    String[] transportType = {"Private", "Public", "Motorcycle"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonTransportBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinnerTransport;
        amountEditText = binding.amountValueTransport;
        button = binding.calculateButtonTransport;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, transportType);
        typeSpinner.setAdapter(transportAdapter);

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

        if (typeValue.equals("Private")) {
            footprintValue = amount * 5.8 / 100 * 2.3035;
        } else if (typeValue.equals("Public")) {
            footprintValue = amount * 18.181818182 / 100 * 2.3035;
        } else if (typeValue.equals("Motorcycle")) {
            footprintValue = amount * 1.8867924528 / 100 * 2.3035;
        }

        databaseReference.child(currentUser).child("transportTotal").setValue(String.valueOf(footprintValue));
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}