package com.level_zero.greeniq.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.level_zero.greeniq.LanguageManager;
import com.level_zero.greeniq.LoginActivity;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LanguageManager languageManager = new LanguageManager(context);
        languageManager.updateResource(languageManager.getLang());
    }
    private FragmentSettingsBinding binding;

    private ImageView back;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        back = binding.back1;

        binding.editProfile.setOnClickListener(view -> NavHostFragment
            .findNavController(SettingsFragment.this)
            .navigate(R.id.action_settingsFragment_to_editProfileFragment));
        binding.aboutButton.setOnClickListener(view -> aboutApplication());

        binding.logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        binding.editLanguage.setOnClickListener(view -> NavHostFragment
            .findNavController(SettingsFragment.this)
            .navigate(R.id.action_settingsFragment_to_editLanguageFragment));

        binding.editTheme.setOnClickListener(v -> NavHostFragment
            .findNavController(SettingsFragment.this)
            .navigate(R.id.action_settingsFragment_to_editThemeFragment));

        return binding.getRoot();
    }

    private void aboutApplication() {
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_about_application, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        AppCompatButton okButton = customLayout.findViewById(R.id.button);

        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        okButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setOnClickListener(view1 -> NavHostFragment
            .findNavController(this)
            .popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}