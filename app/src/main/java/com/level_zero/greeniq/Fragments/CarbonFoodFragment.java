package com.level_zero.greeniq.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.History;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFoodBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarbonFoodFragment extends Fragment {

    private FragmentCarbonFoodBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Spinner typeSpinner;
    private EditText amountEditText;
    private Button button;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ImageView back;
    private ImageSlider imageSlider;

    private String typeValue, currentUser;
    double footprintFood;

    private final String[] foodType = {"Pork", "Poultry", "Beef", "Fish", "Vegetables"};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFoodBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinnerFood;
        amountEditText = binding.amountValueFood;
        button = binding.calculateButtonFood;
        back = binding.back3;
        imageSlider = binding.imageFood;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, foodType);
        typeSpinner.setAdapter(foodAdapter);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F269851593_452438752991494_2277299469512431284_n.png?alt=media&token=99085bd4-ee5c-4692-acfc-d5cde0041618", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336170331_227998702942833_4600141810756095171_n.png?alt=media&token=2dc96c4b-ae16-4324-a293-952e9890dcb0", null, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarbonFoodFragment.this).navigate(R.id.action_carbonFoodFragment_to_carbonFootprintFragment);
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

                Toast.makeText(getActivity(), "Your food carbon footprint is " + valueFood + " Kg CO", Toast.LENGTH_SHORT).show();

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