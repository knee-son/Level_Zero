package com.level_zero.greeniq;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class LanguageManager {
    private final Context ct;
    private final SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx){
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocales(localeList);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);
        Log.d("LanguageManager", "Language updated to: " + code);
    }

    public String getLang()
    {
       return sharedPreferences.getString("lang","en");
    }
    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.apply();
    }
}
