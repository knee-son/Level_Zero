package com.level_zero.greeniq.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
    private Button eng;
    private Button fil;
    private Button btn;
    private TextView txt;
    private ImageView back;

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
        back = binding.back1;

        LanguageManager lang = new LanguageManager(getContext());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditLanguageFragment.this).navigate(R.id.action_editLanguageFragment_to_settingsFragment);
            }
        });

        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            btn.setText("DONE");
            txt.setText("LANGUAGE");

            //getParentFragmentManager().beginTransaction().detach(this).attach(this).commit();
        });

        fil.setOnClickListener(view -> {
            lang.updateResource("fil");
            btn.setText("ITAPOS");
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