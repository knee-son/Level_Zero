package com.level_zero.greeniq.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentEditThemeBinding;

public class EditThemeFragment extends Fragment {

    private FragmentEditThemeBinding binding;
    private RadioGroup btn_theme;

    public EditThemeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String theme;
        binding = FragmentEditThemeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        btn_theme = view.findViewById(R.id.btn_theme);
        btn_theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int theme;
                switch (checkedId) {
                    case R.id.btnDark:
                        theme = AppCompatDelegate.MODE_NIGHT_YES;
                        break;
                    case R.id.btnLight:
                        theme = AppCompatDelegate.MODE_NIGHT_NO;
                        break;
                    default:
                        theme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                }
                Log.d("EditThemeFragment","isChecked:" + checkedId + " theme:" + theme);
                AppCompatDelegate.setDefaultNightMode(theme);
            }
        });
       /* btn_theme.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int selectedBtnId, boolean isChecked) {
                int theme;
                if (isChecked) {
                    switch (selectedBtnId) {
                        case R.id.btnDark:
                            theme = AppCompatDelegate.MODE_NIGHT_YES;
                            break;
                        case R.id.btnLight:
                            theme = AppCompatDelegate.MODE_NIGHT_NO;
                            break;
                        default:
                            theme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                    }
                } else {
                    theme = AppCompatDelegate.MODE_NIGHT_NO;
                }
                Log.d("EditThemeFragment","isChecked:" + isChecked + " theme:" + theme);
                AppCompatDelegate.setDefaultNightMode(theme);
            }
        });*/

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EditThemeFragment.this).navigate(R.id.action_editThemeFragment_to_settingsFragment);

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
