package org.tyaa.training.client.android.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.interfaces.IHttpActions;
import org.tyaa.training.client.android.handlers.IBaseActionConsequencesHandler;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.serde.JsonSerde;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Действия по протоколу Http
 * */
public class HttpActions implements IHttpActions {

    // Клиент для выполнения любых запросов по протоколу HTTP
    private final OkHttpClient mClient;
    // Настройка вывода сведений об HTTP-запросах и ответах в стандартный поток вывода Android - Logcat
    private final HttpLoggingInterceptor mLogger;

    public HttpActions() {
        mLogger = new HttpLoggingInterceptor(s -> Log.d("HTTP LOG", s)).setLevel(HttpLoggingInterceptor.Level.BODY);
        mClient = new OkHttpClient.Builder().addInterceptor(mLogger).build();
    }

    @Override
    public void doRequest(String url, IResponseHandler handler) {
        final Request.Builder builder = new Request.Builder();
        attachJSessionIdIfExists(builder);
        builder.url(url);
        mClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(HttpActions.this.httpErrorCodeToMessage(response.code()));
                } else {
                    saveJSessionIdIfFetched(response);
                    handler.onSuccess();
                }
            }
        });
    }

    @Override
    public void doRequest(String url, String data, IResponseHandler handler) {
        final Request.Builder builder = new Request.Builder();
        attachJSessionIdIfExists(builder);
        builder.url(url);
        builder.post(RequestBody.create(MediaType.parse(App.getContext().getString(R.string.http_mediatype_json)), data));
        mClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(HttpActions.this.httpErrorCodeToMessage(response.code()));
                    handleValidationErrorsIfExists(response, handler);
                } else {
                    saveJSessionIdIfFetched(response);
                    handler.onSuccess();
                }
            }
        });
    }

    @Override
    public void doRequestForResult(String url, IResultHandler<String> handler) {
        final Request.Builder builder = new Request.Builder();
        attachJSessionIdIfExists(builder);
        builder.url(url);
        mClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(HttpActions.this.httpErrorCodeToMessage(response.code()));
                } else {
                    try {
                        ResponseBody responseBody = response.body();
                        saveJSessionIdIfFetched(response);
                        handler.onSuccess(responseBody.string());
                    } catch (IOException e) {
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_response_data_read_failed) + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void doRequestForResult(String url, String data, IResultHandler<String> handler) {
        final Request.Builder builder = new Request.Builder();
        attachJSessionIdIfExists(builder);
        builder.url(url);
        builder.post(RequestBody.create(MediaType.parse(App.getContext().getString(R.string.http_mediatype_json)), data));
        mClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(HttpActions.this.httpErrorCodeToMessage(response.code()));
                    handleValidationErrorsIfExists(response, handler);
                } else {
                    try {
                        ResponseBody responseBody = response.body();
                        saveJSessionIdIfFetched(response);
                        handler.onSuccess(responseBody.string());
                    } catch (IOException e) {
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_response_data_read_failed) + e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void doSimpleSpringSecurityLoginRequest(String url, String login, String password, IResponseHandler handler) {
        final RequestBody formBody = new FormBody.Builder()
                .add("username", login)
                .add("password", password).build();
        final Request.Builder builder = new Request.Builder();
        builder.url(url).post(formBody);
        mClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(HttpActions.this.httpErrorCodeToMessage(response.code()));
                } else {
                    saveJSessionIdIfFetched(response);
                    handler.onSuccess();
                }
            }
        });
    }

    /**
     * Если объект отклика сервера содержит установку сессионного cookie -
     * сохранить значение JSESSIONID в словарь предпочтений приложения
     * */
    private void saveJSessionIdIfFetched(Response response) {
        // попытка получение всех значений заголовка http-ответа под ключом "Set-Cookie"
        List<String> cookielist = response.headers().values("Set-Cookie");
        // если найдено хотя бы одно значение
        if (!cookielist.isEmpty()) {
            // получить первое значение
            final String firstCokieSectionString = (cookielist.get(0).split(";"))[0];
            // если оно начинается с подстроки "JSESSIONID=", то есть содержит значение сессионного
            // cookie, устанавливаемого серверным приложением, работающим на платформе Java
            if (firstCokieSectionString.startsWith(String.format("%s=", App.Preference.JSESSIONID))) {
                // выделить код сеанса
                String jsessionid = firstCokieSectionString.split("=")[1];
                // и сохранить его в словарь предпочтений приложения
                App.getPreferences().edit()
                        .putString(App.Preference.JSESSIONID, jsessionid)
                        .apply();
            }
        }
    }
    /**
     * Присоединить к http-запросу cookie JSESSIONID,
     * если его значение присутствует в словаре предпочтений приложения
     * */
    private void attachJSessionIdIfExists(Request.Builder builder) {
        // если значение под ключом JSESSIONID присутствует в словаре предпочтений приложения
        if (App.getPreferences().contains(App.Preference.JSESSIONID)) {
            // добавить к объекту-строителю http-запроса заголовок - cookie JSESSIONID
            builder.addHeader(
                    "Cookie",
                    String.format(
                            "%s=%s",
                            App.Preference.JSESSIONID,
                            App.getPreferences().getString(App.Preference.JSESSIONID, "")
                    )
            );
        }
    }
    /**
     * Обработать ошибки серверной валидации, если они присутствуют в ответе сервера
     * */
    private void handleValidationErrorsIfExists(Response response, IBaseActionConsequencesHandler actionConsequencesHandler) {
        if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST) {
            ResponseModel<List<String>> responseModel;
            try {
                responseModel = JsonSerde.parseWithListContent(response.body().string(), ResponseModel.class, String.class);
                actionConsequencesHandler.onValidationErrors(responseModel.getData());
            } catch (Exception ex) {
                Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                actionConsequencesHandler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
            }
        }
    }
}
