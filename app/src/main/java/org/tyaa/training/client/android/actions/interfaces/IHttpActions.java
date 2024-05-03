package org.tyaa.training.client.android.actions.interfaces;

import android.util.Log;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IBaseActionConsequencesHandler;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;

import java.net.HttpURLConnection;
import java.util.List;

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
     * Выполнить запрос простой аутентификации в системе SpringSecurity
     * @param url адрес
     * @param login имя учётной записи пользователя
     * @param password пароль учётной записи пользователя
     * @param handler обработчик отклика
     * */
    void doSimpleSpringSecurityLoginRequest(String url, String login, String password, IResponseHandler handler);

    /**
     * Действие в случае провала выполнения операции по протоколу HTTP
     * с вызовом следующего обработчика
     * @param errorMessage текст сообщения об ошибке
     * @param actionConsequencesHandler обработчик для дальнейших действий
     * */
    default void onHttpFailure(IBaseActionConsequencesHandler actionConsequencesHandler, String errorMessage) {
        Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_request_failed), errorMessage);
        actionConsequencesHandler.onFailure(App.getContext().getString(R.string.message_error_http_request_failed));
    }

    /**
     * Преобразователь статус-кода ошибки из ответа на запрос по протоколу HTTP
     * в текст сообщения об ошибке
     * */
    default String httpErrorCodeToMessage(int errorCode) {
        String errorMessage;
        switch (errorCode) {
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                errorMessage = App.getContext().getString(R.string.message_error_http_response_unauthorized_full);
                break;
            case HttpURLConnection.HTTP_FORBIDDEN:
                errorMessage = App.getContext().getString(R.string.message_error_http_response_forbidden_full);
                break;
            case HttpURLConnection.HTTP_CONFLICT:
                errorMessage = App.getContext().getString(R.string.message_error_http_response_conflict);
                break;
            case HttpURLConnection.HTTP_BAD_REQUEST:
                errorMessage = App.getContext().getString(R.string.message_error_validation_errors);
                break;
            default:
                errorMessage = App.getContext().getString(R.string.message_error_http_action_failed);
        }
        Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_request_failed), errorMessage);
        return errorMessage;
    }

    /**
     * Действие в случае наличия ошибок валидации с вызовом следующего обработчика
     * @param actionConsequencesHandler обработчик для дальнейших действий
     * @param validationErrors список ошибок валидации
     * */
    default void onValidationErrors(IBaseActionConsequencesHandler actionConsequencesHandler, List<String> validationErrors) {
        actionConsequencesHandler.onValidationErrors(validationErrors);
    }
}
