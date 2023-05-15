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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentEditLanguageBinding binding;
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
        Button eng = binding.btnEng;
        Button fil = binding.btnFil;
        ImageView back = binding.back1;

        LanguageManager lang = new LanguageManager(getContext());

        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            btn.setText(R.string.done);
            txt.setText(R.string.language);
        });

        fil.setOnClickListener(view -> {
            lang.updateResource("fil");
            btn.setText(R.string.done);
            txt.setText(R.string.language);
        });

        back.setOnClickListener(v -> NavHostFragment
            .findNavController(this)
            .popBackStack());

        btn.setOnClickListener(v -> NavHostFragment
            .findNavController(this)
            .popBackStack());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}