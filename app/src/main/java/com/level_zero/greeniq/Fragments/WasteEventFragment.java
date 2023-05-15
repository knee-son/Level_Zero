package com.level_zero.greeniq.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.databinding.FragmentWasteEventBinding;

public class WasteEventFragment extends Fragment {

    private FragmentWasteEventBinding binding;
    private TextView title, details, startDate, endDate;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWasteEventBinding.inflate(inflater, container, false);

        title = binding.titleEvent;
        details = binding.detailsEvent;
        startDate = binding.startDate;
        endDate = binding.endDate;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://greeniq-ce821-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference databaseReference = firebaseDatabase.getReference("Event");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fetchDescription = snapshot.child("description").getValue(String.class);
                String fetchEndDate = snapshot.child("endDate").getValue(String.class);
                String fetchStartDate = snapshot.child("startDate").getValue(String.class);
                String fetchTitle = snapshot.child("title").getValue(String.class);

                title.setText(fetchTitle);
                details.setText(fetchDescription);
                startDate.setText(fetchStartDate);
                endDate.setText(fetchEndDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnBack.setOnClickListener(v -> NavHostFragment
            .findNavController(this)
            .popBackStack());

        return binding.getRoot();
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}