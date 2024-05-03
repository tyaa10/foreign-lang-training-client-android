package org.tyaa.training.client.android.handlers;

/**
 * Абстракция обработчика полученных данных
 * @param <T> тип полученных данных
 * */
public interface IResultHandler<T> extends IBaseActionConsequencesHandler {
    /**
     * Действие в случае успешно полученных данных
     * @param result полученные данные
     * */
    void onSuccess(T result);
}