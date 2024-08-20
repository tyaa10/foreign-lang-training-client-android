package org.tyaa.training.client.android.utils;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Воспроизведение медиа-данных
 * */
public class Player {

    public static void playAudio(String audioBase64) throws IOException {
        // создание объекта стандартного проигрывателя медиа-данных
        MediaPlayer mediaPlayer = new MediaPlayer();
        // сброс старых данных для воспроизведения
        mediaPlayer.reset();
        // настройка на воспроизведение звуковых данных
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        // установка новых данных для воспроизведения
        mediaPlayer.setDataSource(String.format("data:audio/mp3;base64,%s", audioBase64));
        // установка обработчика события жизненного цикла проигрывателя,
        // когда заканчивается воспроизщведение звуковых данных
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // освобождение ресурсов проигрывателя
                mediaPlayer.stop();
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        });
        // подготовка данных к воспроизведению
        mediaPlayer.prepare();
        // начало воспроизведения аудио
        mediaPlayer.start();
    }
}
