package org.tyaa.training.client.android.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.tyaa.training.client.android.R;

public class UIActions {
    public static void showError(Context context, String errorMessage) {
        Log.println(Log.ERROR, context.getString(R.string.message_error), errorMessage);
        UIActionsRunner.run(() -> Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show());
    }
    public static void showInfo(Context context, String infoMessage) {
        Log.println(Log.INFO, "Information", infoMessage);
        UIActionsRunner.run(() -> Toast.makeText(context, infoMessage, Toast.LENGTH_LONG).show());
    }
}
