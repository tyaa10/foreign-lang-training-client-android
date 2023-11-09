package org.tyaa.training.client.android.handlers;

/**
 * Абстракция обработчика отклика репозитория в ответ на запрос выполнения операции
 * */
public interface IResponseHandler {
    /**
     * Действие в случае успешно выполненной операции
     * */
    void onSuccess();
    /**
     * Действие в случае провала получения данных
     * @param errorMessage текст сообщения об ошибке
     * */
    void onFailure(String errorMessage);
}
