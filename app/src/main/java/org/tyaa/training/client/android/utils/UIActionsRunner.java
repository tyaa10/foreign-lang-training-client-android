package org.tyaa.training.client.android.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Запуск действий в главном потоке выполнения (графического пользовательского интерфейса, UI)
 * */
public class UIActionsRunner {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * Запустить действие, не возвращающее результат
     * @param action реализация стандартного интерфейса Runnable с действиями в UI приложения
     * */
    public static void run(Runnable action) {
        mHandler.post(action);
    }
}
