package org.tyaa.training.client.android.services;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.parsers.JsonParser;
import org.tyaa.training.client.android.services.interfaces.IAuthService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Реализация репозитория ролей, использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpAuthService implements IAuthService {
    private final HttpActions actions;
    public HttpAuthService() {
        actions = new HttpActions();
    }

    @Override
    public void getRoles(IResultHandler<List<RoleModel>> handler) {

        actions.doRequestForResult(
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
                        handler.onSuccess(JsonParser.parseList(result, RoleModel.class));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_request_failed), errorMessage);
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_request_failed));
                    }
                }
        );
    }
    @Override
    public void checkUser(IResultHandler<UserModel> handler) {
        actions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_users_check_uri)
                ),
                new IResultHandler<>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("Unauthorized")) {
                            handler.onSuccess(null);
                        } else {
                            handler.onSuccess(JsonParser.parseObject(result, UserModel.class));
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_http_request_failed), errorMessage);
                        handler.onFailure(App.getContext().getString(R.string.message_error_http_request_failed));
                    }
                }
        );
    }
}
