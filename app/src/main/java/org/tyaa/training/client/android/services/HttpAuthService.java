package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.IAuthService;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

/**
 * Реализация репозитория ролей, использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpAuthService implements IAuthService {

    private final HttpActions mActions;

    public HttpAuthService() {
        mActions = new HttpActions();
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
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_users_check_uri)
                ),
                new IResultHandler<>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            ResponseModel<UserModel> responseModel = JsonSerde.parseWithSingleContent(result, ResponseModel.class, UserModel.class);
                            handler.onSuccess(responseModel.getData());
                        } catch (JsonProcessingException ex) {
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                            handler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        if (errorMessage.equals(App.getContext().getString(R.string.message_error_http_response_unauthorized_full))) {
                            handler.onSuccess(null);
                        } else {
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
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onFailure(String errorMessage) {

                                    }
                                });
                                handler.onSuccess();
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                mActions.onHttpFailure(handler, errorMessage);
                            }

                            @Override
                            public void onValidationErrors(List<String> validationErrors) {
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
                    @Override
                    public void onSuccess() {
                        // если значение под ключом JSESSIONID присутствует в словаре предпочтений приложения
                        if (App.getPreferences().contains(App.Preference.JSESSIONID)) {
                            // удалить код сеанса JSESSIONID из словаря предпочтений приложения
                            App.getPreferences().edit().remove(App.Preference.JSESSIONID).apply();
                        }
                        handler.onSuccess();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        mActions.onHttpFailure(handler, errorMessage);
                    }
                }
        );
    }
}
