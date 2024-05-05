package org.tyaa.training.client.android.handlers;

import android.util.Log;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;

import java.util.List;

/**
 * Абстракция обработчика отклика службы в ответ на запрос выполнения операции любого типа
 * */
public interface IBaseActionConsequencesHandler {
    /**
     * Действие в случае провала выполнения операции
     * @param errorMessage текст сообщения об ошибке
     * */
    void onFailure(String errorMessage);
    /**
     * Действие в случае наличия ошибок валидации
     * @param validationErrors список ошибок валидации
     * */
    default void onValidationErrors(List<String> validationErrors) {
        if(validationErrors.size() > 0) {
            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_validation_errors), ": ");
        }
        for (String validationError : validationErrors) {
            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_validation_error), validationError);
        }
    }
}
