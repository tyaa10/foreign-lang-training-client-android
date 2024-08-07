package org.tyaa.training.client.android.utils;

import android.widget.EditText;

public class UIDataExtractor {
    public static String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }
}
