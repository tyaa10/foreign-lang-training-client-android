package org.tyaa.training.client.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.blongho.country_data.World;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Главный класс приложения
 * */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static SharedPreferences mPreferences;

    @Override
    public void onCreate() {

        super.onCreate();

        // Настройка системы хранения предпочтений приложения в зашифрованном виде в ОС Android
        mContext = getApplicationContext();
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            mPreferences = EncryptedSharedPreferences.create(
                    "FLT_ANDROID_ENCRYPTED_PREFS",
                    masterKeyAlias,
                    App.getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        // загрузка изображений флагов всех стран
        World.init(getApplicationContext());
    }

    /**
     * Получение контекста приложения
     * */
    public static Context getContext() {
        return mContext;
    }

    /**
     * Получение объекта доступа к словарю предпочтений приложения
     * */
    public static SharedPreferences getPreferences() {
        return mPreferences;
    }

    /**
     * Названия ключей предпочтений приложения
     * */
    public static class Preference {
        public static final String JSESSIONID = "JSESSIONID";
    }
}
