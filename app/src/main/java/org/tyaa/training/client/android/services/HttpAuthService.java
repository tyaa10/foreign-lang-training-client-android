package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.actions.interfaces.IHttpActions;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.IAuthService;

import java.util.List;
import java.util.Objects;

/**
 * Реализация репозитория ролей, использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpAuthService implements IAuthService {

    private final IHttpActions mActions;
    private final IState mState;

    public HttpAuthService() {
        mActions = new HttpActions();
        mState = new InMemoryLocalState();
    }

    @Override
    public void getRoles(IResultHandler<List<RoleModel>> handler) {

        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_roles_uri)
                ),
                new IResultHandler<>() {

                    @Override
                    public void onSuccess(String result) {
                        // имитация длительного ожидания отклика сервера для проверки поведения
                        // представления в этой ситуации
                        /* try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } */

                        /* if (result.equals(String.valueOf(HttpURLConnection.HTTP_UNAUTHORIZED))) {
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_reponse_unauthorized), App.getContext().getString(R.string.message_error_http_reponse_unauthorized_full));
                            handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_unauthorized_full));
                        } else if (result.equals(String.valueOf(HttpURLConnection.HTTP_FORBIDDEN))) {
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_reponse_forbidden), App.getContext().getString(R.string.message_error_http_reponse_forbidden_full));
                            handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_forbidden_full));
                        } else {
                            ResponseModel<List<RoleModel>> responseModel;
                            try {
                                responseModel = JsonSerde.parseWithListContent(result, ResponseModel.class, RoleModel.class);
                                handler.onSuccess(responseModel.getData());
                            } catch (Exception ex) {
                                Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                                handler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
                            }
                        } */
                        ResponseModel<List<RoleModel>> responseModel;
                        try {
                            responseModel = JsonSerde.parseWithListContent(result, ResponseModel.class, RoleModel.class);
                            handler.onSuccess(responseModel.getData());
                        } catch (Exception ex) {
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                            handler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        mActions.onHttpFailure(handler, errorMessage);
                    }
                }
        );
    }
    @Override
    public void checkUser(IResultHandler<UserModel> handler) {
        // обращение на сервер - попытка получить данные текущего пользователя
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_users_check_uri)
                ),
                new IResultHandler<>() {

                    @Override
                    public void onSuccess(String result) {
                        // Если данные получены - попытаться десериализовать их
                        // из json-строки в Java-объект модели
                        try {
                            ResponseModel<UserModel> responseModel =
                                    JsonSerde.parseWithSingleContent(result, ResponseModel.class, UserModel.class);
                            // Если десериализация выполнилась без выброса исключения:
                            // 1. установить логин текущего пользователя в состояние приложения
                            mState.setLogin(responseModel.getData().getName());
                            // 2. вызвать обработчик результата успешного действия
                            // с передачей ему данных пользователя
                            handler.onSuccess(responseModel.getData());
                        } catch (JsonProcessingException ex) {
                            // Иначе - вывести отладочные данные об исключении в консоль ОС
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                            // и вызвать обработчик провала десериализации
                            handler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // если текст сообщения об ошибке - отсутствие аутентификации
                        if (errorMessage.equals(App.getContext().getString(R.string.message_error_http_response_unauthorized_full))) {
                            // вызвать обработчик результата успешного действия
                            // с передачей ему пустого значения вместо данных пользователя,
                            // что означает "вход в учётную запись не выполнен"
                            handler.onSuccess(null);
                        } else {
                            // вызвать следующий обработчик с полученным текстом сообщения об ошибке
                            mActions.onHttpFailure(handler, errorMessage);
                        }
                    }
                }
        );
    }

    @Override
    public void signIn(String login, String password, IResponseHandler handler) {
        mActions.doSimpleSpringSecurityLoginRequest(
                App.getContext().getString(R.string.network_base_server_url)
                        .replace("api", App.getContext().getString(R.string.network_simple_spring_security_login_uri)),
                login,
                password,
                new IResponseHandler() {

                    @Override
                    public void onSuccess() {
                        // Если получен отклик об успешно выполненном действии входа в учётную запись:
                        // 1. установить логин текущего пользователя в состояние приложения
                        mState.setLogin(login);
                        // 2. вызвать обработчик отклика об успешного выполненном действии
                        handler.onSuccess();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // если текст сообщения об ошибке - отсутствие аутентификации
                        if (errorMessage.equals(
                                App.getContext().getString(R.string.message_error_http_response_unauthorized_full))
                        ) {
                            // вызвать следующий обработчик с текстом сообщения о неправильном имени и/или пароле
                            mActions.onHttpFailure(handler, App.getContext().getString(R.string.message_error_http_response_auth_wrong_credentials));
                        } else {
                            // иначе - с полученным текстом сообщения об ошибке
                            mActions.onHttpFailure(handler, errorMessage);
                        }
                    }
                }
        );
    }

    @Override
    public void signUp(String login, String password, IResponseHandler handler) {
        try {
                final String userModelJsonString =
                    JsonSerde.serialize(UserModel.builder().name(login).password(password).build());
                mActions.doRequest(
                        String.format("%s/%s",
                                App.getContext().getString(R.string.network_base_server_url),
                                App.getContext().getString(R.string.network_users_uri)
                        ),
                        userModelJsonString,
                        new IResponseHandler() {

                            @Override
                            public void onSuccess() {
                                // автоматический вход в учётную запись после получения
                                // от сервера ответа о её успешном создании
                                signIn(login, password, new IResponseHandler() {
                                    // Если получен отклик об успешно выполненном действии входа в учётную запись
                                    @Override
                                    public void onSuccess() {
                                        // 1. установить логин текущего пользователя в состояние приложения
                                        mState.setLogin(login);
                                        // 2. вызвать обработчик отклика об успешно выполненном действии регистрации
                                        // (только после получения положительного отклика сервера
                                        // на первый вход в созданную учётную запись)
                                        handler.onSuccess();
                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {
                                        // если текст сообщения об ошибке - отсутствие аутентификации
                                        if (errorMessage.equals(
                                                App.getContext().getString(R.string.message_error_http_response_unauthorized_full))
                                        ) {
                                            // вызвать следующий обработчик с текстом сообщения о неправильном имени и/или пароле
                                            mActions.onHttpFailure(handler, App.getContext().getString(R.string.message_error_http_response_auth_wrong_credentials));
                                        } else {
                                            // иначе - с полученным текстом сообщения об ошибке
                                            mActions.onHttpFailure(handler, errorMessage);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                // вызвать следующий обработчик с полученным текстом сообщения об ошибке
                                mActions.onHttpFailure(handler, errorMessage);
                            }

                            @Override
                            public void onValidationErrors(List<String> validationErrors) {
                                // вызвать обработчик ошибок валидации регистрационных данных
                                handler.onValidationErrors(validationErrors);
                            }
                        }
                );
        } catch (JsonProcessingException e) {
            handler.onFailure(App.getContext().getString(R.string.message_error_serialization));
        }
    }

    @Override
    public void signOut(IResponseHandler handler) {
        mActions.doRequest(
                App.getContext().getString(R.string.network_base_server_url)
                        .replace("api", App.getContext().getString(R.string.network_simple_spring_security_logout_uri)),
                new IResponseHandler() {
                    // Если получен отклик об успешно выполненном действии выхода из учётной записи:
                    @Override
                    public void onSuccess() {
                        // 1. сбросить логин текущего пользователя в состоянии приложения
                        mState.setLogin(null);
                        // 2. если значение под ключом JSESSIONID присутствует в словаре предпочтений приложения
                        if (App.getPreferences().contains(App.Preference.JSESSIONID)) {
                            // удалить код сеанса JSESSIONID из словаря предпочтений приложения
                            App.getPreferences().edit().remove(App.Preference.JSESSIONID).apply();
                        }
                        // 3. вызвать обработчик отклика об успешно выполненном действии выхода
                        handler.onSuccess();
                    }
                    // Если получен отклик о провале выполнения действия выхода из учётной записи
                    @Override
                    public void onFailure(String errorMessage) {
                        // вызвать следующий обработчик с полученным текстом сообщения об ошибке
                        mActions.onHttpFailure(handler, errorMessage);
                    }
                }
        );
    }
}
