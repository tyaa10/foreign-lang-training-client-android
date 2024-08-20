package org.tyaa.training.client.android.utils;

import android.widget.EditText;

/**
 * Извлечение значений простых типов из содержимого виджетов
 * */
public class UIDataExtractor {

    /**
     * Получить значение строкового типа из содержимого виджета ввода/редактированя текста
     * @param editText ссылка на объект доступа к виджету ввода/редактированя текста
     * */
    public static String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }
}
