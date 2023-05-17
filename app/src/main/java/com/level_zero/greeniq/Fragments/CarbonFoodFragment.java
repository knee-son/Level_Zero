package com.level_zero.greeniq.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonFoodBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CarbonFoodFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }

    private FragmentCarbonFoodBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText amountEditText;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String currentUser;
    private FragmentActivity thisActivity;
    double footprintFood;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonFoodBinding.inflate(inflater, container, false);
        thisActivity = requireActivity();

        amountEditText = binding.amountValueFood;
        Button button = binding.calculateButtonFood;
        ImageView back = binding.back3;
        ImageSlider imageSlider = binding.imageFood;

        Bundle bundle = requireActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.US);

        String[] foodType;
        if (Locale.getDefault().getLanguage().equals("fil"))
            foodType = new String[] {"Baboy", "Manok", "Baka", "Isda", "Gulay"};
        else
            foodType = new String[] {"Pork", "Poultry", "Beef", "Fish", "Vegetables"};

        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, foodType);
        foodAdapter.setDropDownViewResource(R.layout.spinner_item_custom);

        Spinner typeSpinner = binding.typeSpinnerFood;
        typeSpinner.setAdapter(foodAdapter);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F269851593_452438752991494_2277299469512431284_n.png?alt=media&token=99085bd4-ee5c-4692-acfc-d5cde0041618", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336170331_227998702942833_4600141810756095171_n.png?alt=media&token=2dc96c4b-ae16-4324-a293-952e9890dcb0", null, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        button.setOnClickListener(view -> {
            String fetchAmount = amountEditText.getText().toString();

            if(fetchAmount.equals("")){
                amountEditText.setError(getString(R.string.youve_left_a_field_empty));
            }else{
                amountEditText.setError(null);
                valueDatabase();
                addToHistory();
            }
        });

        back.setOnClickListener(v -> NavHostFragment
            .findNavController(this)
            .popBackStack());

        return binding.getRoot();
    }

    private void addToHistory() {
        databaseReference.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String transportData = snapshot.child("transportTotal").getValue(String.class);
                String foodData = snapshot.child("foodTotal").getValue(String.class);
                String electricityData = snapshot.child("electricityTotal").getValue(String.class);

                double valueTransport = Double.parseDouble(transportData != null ? transportData : "0");
                double valueFood = Double.parseDouble(foodData != null ? foodData : "0");
                double valueElectricity = Double.parseDouble(electricityData != null ? electricityData : "0");

                double total = valueTransport + valueFood + valueElectricity;
                String currentDate = simpleDateFormat.format(calendar.getTime());

                History history = new History(currentDate, String.valueOf(total));


                if(valueFood == 0)
                {
                    displayErrorDialog();
                    return;
                }else {
                    Toast.makeText(thisActivity, String.format(
                        Locale.US,
                        thisActivity.getString(R.string.your_food_carbon_footprint)+"%.2f Kg CO",
                        valueFood),
                        Toast.LENGTH_SHORT).show();
                }
                firebaseDatabase.getReference("Carbon History").child(currentUser).child(currentDate).setValue(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void valueDatabase() {
        double amount;

        try {
            amount = Double.parseDouble(amountEditText.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorDialog();
            return;
        }

//      pork, poultry, beef, fish, vegetables
        double[] values = {0.0076, 0.0069, 0.027, 0.011, 0.002};

        Spinner typeSpinner = binding.typeSpinnerFood;
        footprintFood = amount*values[typeSpinner.getSelectedItemPosition()];

        databaseReference.child(currentUser).child("foodTotal").setValue(String.valueOf(footprintFood));
    }
    private void displayErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error!");
        builder.setMessage(requireActivity().getString(R.string.invalid_input));
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}