package com.level_zero.greeniq.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentCarbonTransportBinding;;import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CarbonTransportFragment extends Fragment {

    private LanguageManager languageManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentCarbonTransportBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private EditText amountEditText;
    private Button button;
    private Spinner typeSpinner;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ImageSlider imageSlider;
    private ImageView back;
    String typeValue, currentUser;
    double footprintValue;

    String[] transportType = {"Private", "Public", "Motorcycle"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCarbonTransportBinding.inflate(inflater, container, false);

        typeSpinner = binding.typeSpinnerTransport;
        amountEditText = binding.amountValueTransport;
        button = binding.calculateButtonTransport;
        imageSlider = binding.imageTransport;
        back = binding.back1;

        Bundle bundle = getActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy");

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336184726_126461133555598_279106848359392906_n.png?alt=media&token=f15ef79b-02ba-48fd-9c18-10de6a7c06e1", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336226796_670980624799049_635249469790701069_n.png?alt=media&token=5a9f5492-6918-483f-bcdb-c5f1f561dfe8", null, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CarbonTransportFragment.this).navigate(R.id.action_carbonTransportFragment_to_carbonFootprintFragment);
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

                if(valueTransport == 0)
                {
                    displayErrorDialog("Invalid input:\nPlease enter a numeric value.");
                    return;
                }else{
                    Toast.makeText(getActivity(), String.format("Your transport carbon footprint is %.2f Kg CO", valueTransport), Toast.LENGTH_SHORT).show();
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

        double amount = 0;

        try {
            amount = Double.parseDouble(amountEditText.getText().toString());;
        } catch (NumberFormatException e) {
            displayErrorDialog("Invalid input:\nPlease enter a numeric value.");
            return;
        }

        if (typeValue.equals("Private")) {
            footprintValue = amount * (5.8 / 100 * 2.3035);
        } else if (typeValue.equals("Public")) {
            footprintValue = amount * (18.181818182 / 100 * 2.3035);
        } else if (typeValue.equals("Motorcycle")) {
            footprintValue = amount * (1.8867924528 / 100 * 2.3035);
        }

        databaseReference.child(currentUser).child("transportTotal").setValue(String.valueOf(footprintValue));
    }
    private void displayErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error!");

        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}