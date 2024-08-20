package org.tyaa.training.client.android.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.tyaa.training.client.android.R;

public class InfinityProgressToast extends Toast {

    private static TextView mProgressToastText;
    private final Toast mProgressToast;

    public InfinityProgressToast(Context context) {

        super(context);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*
         * Инициализация разметки всплывающего сообщения,
         * устанавливается изображение значка информации в всплывающее сообщение
         */
        View rootView = inflater.inflate(R.layout.widget_infinity_progress_toast, null);

        /*
         * Устанавливается представление,
         * местоположение всплывающего сообщения на экране устройства
         * и длительность существованая сообщения
         */
        this.setView(rootView);
        this.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        this.setDuration(Toast.LENGTH_SHORT);
        mProgressToast = this;
    }

    /*
     * Метод вызова сообщения без установки длительности существования
     * с передачей сообщению текстовой информации в качестве последовательности
     * текстовых символов или строки
     */
    public static InfinityProgressToast makeText(Context context, CharSequence text) {
        InfinityProgressToast result = new InfinityProgressToast(context);
        return result;
    }

    /*
     * Вызов сообщения с установкой длительности существования и
     * с передачей сообщению текстовой информации в качестве последовательности
     * текстовых символов или строки
     */
    public static InfinityProgressToast makeText(Context context, CharSequence text, int duration) {
        InfinityProgressToast result = new InfinityProgressToast(context);
        result.setDuration(duration);
        //mProgressToastText.setText(text);
        return result;
    }

    /*
     * Вызов сообщения без установки длительности существования
     * с передачей сообщению ID текстового ресурса
     */
    public static Toast makeText(Context context, int resId)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId));
    }

    /*
     * Вызов сообщения с установкой длительности существования и
     * с передачей ID текстового ресурса
     */
    public static Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }


}
