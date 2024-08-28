package org.tyaa.training.client.android.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.widgets.InfinityProgressToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Действия, выполняемые гарантированно в основном (графическом) потоке
 * */
public class UIActions {

    private final static Handler mHandler = new Handler();
    private static InfinityProgressToast mInfinityProgressToast = null;
    private static Timer mTimer;
    private static boolean isProgressToastShow = false;

    /**
     * Отобразить всплывающее окно сообщения об ошибке
     * @param context контекст выполнения действия на экране (Application/Activity)
     * @param errorMessage текст ошибки для отображения
     * */
    public static void showError(Context context, String errorMessage) {
        Log.println(Log.ERROR, context.getString(R.string.message_error), errorMessage);
        UIActionsRunner.run(() -> Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show());
    }

    /**
     * Отобразить всплывающее окно информационного сообщения
     * @param context контекст выполнения действия на экране (Application/Activity)
     * @param infoMessage строка информации для отображения
     * */
    public static void showInfo(Context context, String infoMessage) {
        Log.println(Log.INFO, "Information", infoMessage);
        UIActionsRunner.run(() -> Toast.makeText(context, infoMessage, Toast.LENGTH_SHORT).show());
    }

    /**
     * Отображать анимацию ожидания, пока она не будет отменена явно
     * */
    public static void showInfinityProgressToast (final Context context) {

        if (isProgressToastShow == false) {

            mInfinityProgressToast =
                    mInfinityProgressToast.makeText(context, "", Toast.LENGTH_LONG);

            // выдержка перед началом отображения анимации ожидания,
            // так как если ожидание результата действия продлится менее 0.5 секунд,
            // отображать анимацию нет смысла
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            // если за время выдержки анимация была отменена - завершить выполнение метода
            if(mInfinityProgressToast == null) {
                return;
            }

            mTimer = new Timer();

            TimerTask timerTask = new TimerTask() {

                @Override
                public void run() {
                    if (mInfinityProgressToast != null) {
                        try {
                            mInfinityProgressToast.getView()
                                    .findViewById(R.id.widget_infinityProgress_ProgressBar)
                                    .setVisibility(View.VISIBLE);
                            mInfinityProgressToast.show();
                        } catch (Exception ignored) {}
                    }
                }
            };

            mTimer.schedule(timerTask, 0, 4 * 60);
            isProgressToastShow = true;
        }
    }

    /**
     * Прекратить отображение анимации ожидания
     * */
    public static void closeInfinityProgressToast () {

        if (mInfinityProgressToast != null) {

            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    if (mInfinityProgressToast != null) {
                        try {
                            mInfinityProgressToast.getView()
                                    .findViewById(R.id.widget_infinityProgress_ProgressBar)
                                    .setVisibility(View.GONE);
                            mInfinityProgressToast.cancel();
                        } catch (Exception ignored) {}
                        mInfinityProgressToast = null;
                    }

                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }

                    if (mInfinityProgressToast == null && mTimer == null) {
                        isProgressToastShow = false;
                    }
                }
            });
        }
    }
}
