package org.tyaa.training.client.android.utils;

import android.os.Looper;

/**
 * Запуск действий в графическом потоке выполнения
 * */
public class UIRunner {
    /**
     * Запустить действие, не возвращающее результат
     * @param action реализация стандартного интерфейса Runnable с действиями в UI приложения
     * */
    public static void run(Runnable action) {
        Looper.prepare();
        action.run();
        Looper.loop();
    }
}
