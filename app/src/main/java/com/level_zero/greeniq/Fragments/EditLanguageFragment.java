package com.level_zero.greeniq.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;

import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.databinding.FragmentEditLanguageBinding;

public class EditLanguageFragment extends Fragment {

    private @NonNull FragmentEditLanguageBinding binding;
    private ImageButton eng, fil;

    public EditLanguageFragment() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditLanguageBinding.inflate(inflater, container, false);

        eng = binding.btnEng;
        fil = binding.btnFil;
        LanguageManager lang = new LanguageManager(getContext());

        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });

        fil.setOnClickListener(view -> {
            lang.updateResource("fil");
            getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });


        binding.btnTheme.setOnClickListener(view ->
        {

        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}