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
import com.level_zero.greeniq.databinding.FragmentCarbonTransportBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CarbonTransportFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentCarbonTransportBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private EditText amountEditText;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private FragmentActivity thisActivity;
    String currentUser;
    double footprintValue;

    String[] transportType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCarbonTransportBinding.inflate(inflater, container, false);
        thisActivity = requireActivity();

        Spinner typeSpinner = binding.typeSpinnerTransport;
        amountEditText = binding.amountValueTransport;
        Button button = binding.calculateButtonTransport;
        ImageSlider imageSlider = binding.imageTransport;
        ImageView back = binding.back1;

        if (Locale.getDefault().getLanguage().equals("fil"))
            transportType = new String[] {"Pribadong Sasakyan", "Pampublikong Sasakyan", "Motorsiklo"};
        else
            transportType = new String[] {"Private Vehicle", "Public Transport", "Motorcycle"};

        ArrayAdapter<String> transportAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, transportType);
        transportAdapter.setDropDownViewResource(R.layout.spinner_item_custom);
        typeSpinner.setAdapter(transportAdapter);

        Bundle bundle = requireActivity().getIntent().getExtras();
        currentUser = bundle.getString("id");

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.US);

        firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Carbon Data");

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336184726_126461133555598_279106848359392906_n.png?alt=media&token=f15ef79b-02ba-48fd-9c18-10de6a7c06e1", null, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/greeniq-ce821.appspot.com/o/infomercial%2F336226796_670980624799049_635249469790701069_n.png?alt=media&token=5a9f5492-6918-483f-bcdb-c5f1f561dfe8", null, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels,ScaleTypes.CENTER_CROP);
        
        button.setOnClickListener(view -> {
            String fetchAmount = amountEditText.getText().toString();

            if(fetchAmount.equals("")){
                amountEditText.setError("Fill this field.");
            }else{
                amountEditText.setError(null);
                valueDatabase();
                addToHistory();
            }
        });

        back.setOnClickListener(view -> NavHostFragment
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

                if(valueTransport == 0) {
                    displayErrorDialog();
                    return;
                }else{
                    Toast.makeText(thisActivity, String.format(Locale.US,
                        thisActivity.getString(R.string.your_transport_carbon_footprint)+" %.2f Kg CO",
                        valueTransport),
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

        double amount;

        try {
            amount = Double.parseDouble(amountEditText.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorDialog();
            return;
        }

//      private, public, motorcycle
        double[] values = {(5.8/100*2.3035), (18.181818182/100*2.3035), (1.8867924528/100*2.3035)};

        Spinner typeSpinner = binding.typeSpinnerTransport;
        footprintValue = amount*values[typeSpinner.getSelectedItemPosition()];

        databaseReference.child(currentUser).child("transportTotal").setValue(String.valueOf(footprintValue));
    }
    private void displayErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error!");
        builder.setMessage("Invalid input:\nPlease enter a numeric value.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}