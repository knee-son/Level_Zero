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
import com.level_zero.greeniq.databinding.FragmentCarbonElectricityBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CarbonElectricityFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentCarbonElectricityBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private FragmentActivity thisActivity;
    String currentUser;
    double footprintElectricity;
    String[] electricityType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCarbonElectricityBinding.inflate(inflater, container, false);
        thisActivity = requireActivity();

        Spinner typeSpinner = binding.typeSpinnerElectricity;
        Button button = binding.calculateButtonElectricity;
        ImageView back = binding.back2;
        ImageSlider imageSlider = binding.imageElectricity;

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.US);

        Bundle bundle = requireActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F335985215_6529885593705783_8784384603182648526_n.png?alt=media&token=42652e58-6e2e-419a-a7f4-b59818603165", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2Fsave_energy.png?alt=media&token=cbe416fc-5ebf-462d-bfc5-ea739e93c5d2", null, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

        if (Locale.getDefault().getLanguage().equals("fil"))
            electricityType = new String[] {"Marahang Paggamit", "Katamtamang Paggamit", "Mataas na Paggamit"};
        else electricityType = new String[] {"Low Usage", "Medium Usage", "High Usage"};

        ArrayAdapter<String> electricityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, electricityType);
        electricityAdapter.setDropDownViewResource(R.layout.spinner_item_custom);
        typeSpinner.setAdapter(electricityAdapter);

        button.setOnClickListener(view -> {
            valueDatabase();
            addToHistory();
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

                if(valueElectricity == 0)
                {
                    displayErrorDialog();
                    return;
                }else {
                    Toast.makeText(
                        thisActivity,
                        String.format(Locale.US,
                        thisActivity.getString(R.string.your_electricity_carbon_footprint)+"%.2f Kg CO",
                        valueElectricity),
                        Toast.LENGTH_SHORT).show();
                }


                History history = new History(currentDate, String.valueOf(total));

                firebaseDatabase.getReference("Carbon History").child(currentUser).child(currentDate).setValue(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void valueDatabase() {
//      low usage, medium usage, high usage
        double[] electricityValue = {24*1.35, 24*5,  24*17};
        Spinner typeSpinner = binding.typeSpinnerElectricity;
        footprintElectricity = electricityValue[typeSpinner.getSelectedItemPosition()];

        databaseReference.child(currentUser).child("electricityTotal").setValue(String.valueOf(footprintElectricity));
    }
    private void displayErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
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