package org.tyaa.training.client.android.actions.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;

/**
 * Абстракция действий по протоколу Http
 * */
public interface IHttpActions {

    /**
     * Выполнить запрос, не содержащий данные и не предполагающий получение данных
     * @param url адрес
     * @param handler обработчик отклика
     * */
    void doRequest(String url, IResponseHandler handler);

    /**
     * Выполнить запрос, содержащий данные, но не предполагающий получение данных
     * @param url адрес
     * @param data данные для отправки
     * @param handler обработчик отклика
     * */
    void doRequest(String url, String data, IResponseHandler handler);

    /**
     * Выполнить запрос, не содержащий данные, но предполагающий получение данных
     * @param url адрес
     * @param handler обработчик полученных данных
     * */
    void doRequestForResult(String url, IResultHandler<String> handler);

    /**
     * Выполнить запрос, содержащий данные и предполагающий получение данных
     * @param url адрес
     * @param data данные для отправки
     * @param handler обработчик полученных данных
     * */
    void doRequestForResult(String url, String data, IResultHandler<String> handler);

    /**
     * Выполнить запрос, содержащий данные, но не предполагающий получение данных
     * @param url адрес
     * @param login имя учётной записи пользователя
     * @param password пароль учётной записи пользователя
     * @param handler обработчик отклика
     * */
    void doSimpleSpringSecurityLoginRequest(String url, String login, String password, IResultHandler<String> handler);
}
