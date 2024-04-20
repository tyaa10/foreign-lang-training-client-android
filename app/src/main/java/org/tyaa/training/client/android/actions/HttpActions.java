package org.tyaa.training.client.android.actions;

import androidx.annotation.NonNull;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.interfaces.IHttpActions;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Действия по протоколу Http
 * */
public class HttpActions implements IHttpActions {

    private final OkHttpClient client;

    public HttpActions() {
        this.client = new OkHttpClient();
    }

    @Override
    public void doRequest(String url, IResponseHandler handler) {
        final Request.Builder builder = new Request.Builder();
        attachJSessionIdIfExists(builder);
        builder.url(url);
        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
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
        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
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
        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    if(response.code() == 401) {
                        handler.onSuccess("Unauthorized");
                    } else {
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
                    }
                } else {
                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            saveJSessionIdIfFetched(response);
                            handler.onSuccess(responseBody.string());
                        } else {
                            handler.onFailure(App.getContext().getString(R.string.message_error_http_response_nodata));
                        }
                    } catch (IOException e) {
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_response_data_read_failed) + e);
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
        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
                } else {
                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            saveJSessionIdIfFetched(response);
                            handler.onSuccess(responseBody.string());
                        } else {
                            handler.onFailure(App.getContext().getString(R.string.message_error_http_response_nodata));
                        }
                    } catch (IOException e) {
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_response_data_read_failed) + e);
                    }
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
            builder.addHeader(App.Preference.JSESSIONID, App.getPreferences().getString(App.Preference.JSESSIONID, ""));
        }
    }
}
