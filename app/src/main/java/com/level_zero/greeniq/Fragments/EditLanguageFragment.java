package com.level_zero.greeniq.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;

import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentEditLanguageBinding;

public class EditLanguageFragment extends Fragment {

    private LanguageManager languageManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private @NonNull FragmentEditLanguageBinding binding;
    private ImageButton eng, fil;
    private Button btn;
    private TextView txt;

    public EditLanguageFragment() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditLanguageBinding.inflate(inflater, container, false);

        txt = binding.textLanguage;
        btn = binding.btnTheme;
        eng = binding.btnEng;
        fil = binding.btnFil;
        LanguageManager lang = new LanguageManager(getContext());

        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            btn.setText("LOCATION");
            txt.setText("LANGUAGE");

            //getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });

        fil.setOnClickListener(view -> {
            lang.updateResource("fil");
            btn.setText("LOKASYON");
            txt.setText("WIKA");
            //getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EditLanguageFragment.this).navigate(R.id.action_editLanguageFragment_to_homeFragment);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}