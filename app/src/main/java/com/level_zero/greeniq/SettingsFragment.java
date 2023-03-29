package com.level_zero.greeniq;

import static androidx.navigation.Navigation.findNavController;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.level_zero.greeniq.databinding.FragmentAirQualityBinding;
import com.level_zero.greeniq.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private ImageView back;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        back = binding.back1;
        binding.aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                TextView titleView = customLayout.findViewById(R.id.dialog_title);
                TextView messageView = customLayout.findViewById(R.id.dialog_message);
                AppCompatButton okButton = customLayout.findViewById(R.id.button);

                titleView.setText("About Application");
                messageView.setText("[Test Details about the application]");


                builder.setView(customLayout);
                AlertDialog dialog = builder.create();

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingsFragment.this).navigate(R.id.action_settingsFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}