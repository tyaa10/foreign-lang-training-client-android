package org.tyaa.training.client.android.handlers;

/**
 * Абстракция обработчика отклика репозитория в ответ на запрос выполнения операции
 * */
public interface IResponseHandler extends IBaseActionConsequencesHandler {
    /**
     * Действие в случае успешно выполненного действия
     * */
    void onSuccess();
}
