package org.tyaa.training.client.android.utils;

/**
 * Преобразования типов
 * */
public class TypeConverters {

    public static Integer booleanToInteger(Boolean booleanObject) {
        return booleanObject.compareTo(false);
    }
}
