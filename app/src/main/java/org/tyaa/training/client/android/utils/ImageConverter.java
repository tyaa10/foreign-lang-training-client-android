package org.tyaa.training.client.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Преобразования форматов данных изображений
 * */
public class ImageConverter {

    /**
     * Создание base64-строки на основе uri изображения
     * @prop context ссылка на контекст, относительно которого будет получены данные растра изображения на основе его uri
     * @prop uri уникальный идентификатор изображения, на основе которого будет создана base64-строка
     * */
    public static String uriToBase64(Context context, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Создание данных растрового изображения на основе base64-строки изображения
     * @prop context ссылка на контекст, относительно которого будет получены данные растра изображения на основе его uri
     * @prop imageBase64 base64-строка изображения в формате png
     * */
    public static Bitmap base64ToBitmap(Context context, String imageBase64) {
        byte[] decodedString =
                Base64.decode(imageBase64.replace("data:image/png;base64,", ""), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
