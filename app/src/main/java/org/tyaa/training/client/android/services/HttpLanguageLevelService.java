package org.tyaa.training.client.android.services;

import android.util.Log;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageLevelModel;
import org.tyaa.training.client.android.models.LanguageModel;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.ILanguageLevelService;

import java.util.List;
import java.util.Objects;

/**
 * Реализация репозитория языков,
 * использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpLanguageLevelService implements ILanguageLevelService {

    private final HttpActions mActions;

    public HttpLanguageLevelService() {
        mActions = new HttpActions();
    }

    @Override
    public void getLanguageLevels(IResultHandler<List<LanguageLevelModel>> handler) {
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_language_levels_uri)
                ),
                new IResultHandler<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseModel<List<LanguageLevelModel>> responseModel;
                        try {
                            responseModel = JsonSerde.parseWithListContent(result, ResponseModel.class, LanguageLevelModel.class);
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
}
