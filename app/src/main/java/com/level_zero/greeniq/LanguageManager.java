package com.level_zero.greeniq;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class LanguageManager {
    private Context ct;
    public LanguageManager(Context ctx){

        ct = ctx;

    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocales(localeList);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        Log.d("LanguageManager", "Language updated to: " + code);
    }


}
