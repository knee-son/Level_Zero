package com.level_zero.greeniq.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentEditThemeBinding;

public class EditThemeFragment extends Fragment {

    private FragmentEditThemeBinding binding;

    public EditThemeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditThemeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageView back = binding.back1;
        RadioGroup btn_theme = view.findViewById(R.id.btn_theme);

        btn_theme.setOnCheckedChangeListener((group, checkedId) -> {
            int theme1;
            switch (checkedId) {
                case R.id.btnDark:
                    theme1 = AppCompatDelegate.MODE_NIGHT_YES;
                    break;
                case R.id.btnLight:
                    theme1 = AppCompatDelegate.MODE_NIGHT_NO;
                    break;
                default:
                    theme1 = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }
            Log.d("EditThemeFragment","isChecked:" + checkedId + " theme:" + theme1);
            AppCompatDelegate.setDefaultNightMode(theme1);
        });

        binding.btnDone.setOnClickListener(v -> navigateToSettingsFragment());
        back.setOnClickListener(view1 -> navigateToSettingsFragment());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToSettingsFragment() {
        NavHostFragment
            .findNavController(EditThemeFragment.this)
            .navigate(R.id.action_editThemeFragment_to_settingsFragment);
    }
}