package org.tyaa.training.client.android.repositories;

import android.util.Log;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.parsers.JsonParser;
import org.tyaa.training.client.android.repositories.interfaces.IRoleRepository;
import org.tyaa.training.client.android.repositories.interfaces.IUserRepository;

import java.util.List;

/**
 * Реализация репозитория пользователей, использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class UserRepository implements IUserRepository {
    
    private final HttpActions actions = new HttpActions();

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
