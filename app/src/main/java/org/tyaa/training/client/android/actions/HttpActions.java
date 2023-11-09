package org.tyaa.training.client.android.actions;

import androidx.annotation.NonNull;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.interfaces.IHttpActions;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;

import java.io.IOException;

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
        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
                } else {
                    handler.onSuccess();
                }
            }
        });
    }

    @Override
    public void doRequest(String url, String data, IResponseHandler handler) {
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse(App.getContext().getString(R.string.http_mediatype_json)), data))
                        .build()
        ).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.onFailure(App.getContext().getString(R.string.message_error_http_connection_failed) + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful()) {
                    handler.onFailure(App.getContext().getString(R.string.message_error_http_reponse_fail_code) + response.code() + " " + response.message());
                } else {
                    handler.onSuccess();
                }
            }
        });
    }

    @Override
    public void doRequestForResult(String url, IResultHandler<String> handler) {
        client.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
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
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse(App.getContext().getString(R.string.http_mediatype_json)), data))
                        .build()
        ).enqueue(new Callback() {
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
}
