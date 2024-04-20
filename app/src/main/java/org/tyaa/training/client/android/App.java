package org.tyaa.training.client.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Главный класс приложения
 * */
public class App extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            preferences = EncryptedSharedPreferences.create(
                    "FLT_ANDROID_ENCRYPTED_PREFS",
                    masterKeyAlias,
                    App.getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Получение контекста приложения
     * */
    public static Context getContext() {
        return context;
    }
    /**
     * Получение объекта доступа к словарю предпочтений приложения
     * */
    public static SharedPreferences getPreferences() {
        return preferences;
    }
    public static class Preference {
        public static final String JSESSIONID = "JSESSIONID";
    }
}
