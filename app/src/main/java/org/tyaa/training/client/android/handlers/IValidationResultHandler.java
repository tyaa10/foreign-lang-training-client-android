package org.tyaa.training.client.android.handlers;

import java.util.List;

/**
 * Абстракция обработчика результатов валидации
 * */
public interface IValidationResultHandler {
    /**
     * Действие в случае успеха валидации
     * */
    void onValid();
    /**
     * Действие в случае наличия ошибок валидации
     * @param messages список сообщений об ошибках валидации
     * */
    void onInvalid(List<String> messages);
}
