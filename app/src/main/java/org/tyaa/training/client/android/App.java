package org.tyaa.training.client.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Главный класс приложения
 * */
public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Получение контекста приложения
     * */
    public static Context getContext() {
        return context;
    }
}
