package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageModel;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.ILanguageService;
import org.tyaa.training.client.android.services.interfaces.IProfileService;

import java.util.List;
import java.util.Objects;

/**
 * Реализация репозитория языков,
 * использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpLanguageService implements ILanguageService {

    private final HttpActions mActions;

    public HttpLanguageService() {
        mActions = new HttpActions();
    }

    @Override
    public void getLanguages(IResultHandler<List<LanguageModel>> handler) {
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_languages_uri)
                ),
                new IResultHandler<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseModel<List<LanguageModel>> responseModel;
                        try {
                            responseModel = JsonSerde.parseWithListContent(result, ResponseModel.class, LanguageModel.class);
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
