package com.level_zero.greeniq;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppCompat extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource(languageManager.getLang());
    }
}
