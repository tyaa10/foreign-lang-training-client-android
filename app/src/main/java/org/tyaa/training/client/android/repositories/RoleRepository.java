package org.tyaa.training.client.android.repositories;

import android.util.Log;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.repositories.interfaces.IRoleRepository;

/**
 * Реализация репозитория ролей, использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class RoleRepository implements IRoleRepository {
    
    private final HttpActions actions = new HttpActions();

    @Override
    public void getRoles(IResultHandler<String> handler) {

        actions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_roles_uri)
                ),
                new IResultHandler<>() {
                    @Override
                    public void onSuccess(String result) {
                        handler.onSuccess(result);
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
