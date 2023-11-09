package org.tyaa.training.client.android.handlers;

/**
 * Абстракция обработчика полученных данных
 * @param <T> тип полученных данных
 * */
public interface IResultHandler<T> {
    /**
     * Действие в случае успешно полученных данных
     * @param result полученные данные
     * */
    void onSuccess(T result);
    /**
     * Действие в случае провала получения данных
     * @param errorMessage текст сообщения об ошибке
     * */
    void onFailure(String errorMessage);
}