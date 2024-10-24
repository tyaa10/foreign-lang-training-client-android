package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.UserProfileModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.IProfileService;

import java.util.List;
import java.util.Objects;

/**
 * Реализация репозитория профилей пользователей,
 * использующего сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpProfileService implements IProfileService {

    private final HttpActions mActions;

    public HttpProfileService() {
        this.mActions = new HttpActions();
    }

    @Override
    public void getProfiles(IResultHandler<List<UserProfileModel>> handler) {
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_admin_profiles_uri)
                ),
                new IResultHandler<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseModel<List<UserProfileModel>> responseModel;
                        try {
                            responseModel = JsonSerde.parseWithListContent(result, ResponseModel.class, UserProfileModel.class);
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
    public void createProfile(UserProfileModel profileModel, IResultHandler handler) {
        try {
            final String profileModelJsonString = JsonSerde.serialize(profileModel);
            mActions.doRequestForResult(
                    String.format("%s/%s",
                            App.getContext().getString(R.string.network_base_server_url),
                            App.getContext().getString(R.string.network_profiles_uri)
                    ),
                    profileModelJsonString,
                    new IResultHandler<String>() {

                        @Override
                        public void onSuccess(String result) {
                            // десериализация и передача обработчику положительного результата
                            // объекта модели профиля, дополненного сервером (добавлен идентификатор)
                            ResponseModel<UserProfileModel> responseModel;
                            try {
                                responseModel = JsonSerde.parseWithSingleContent(result, ResponseModel.class, UserProfileModel.class);
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
    public void getCurrentUserProfile(IResultHandler<UserProfileModel> handler) {
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_profiles_current_uri)
                ),
                new IResultHandler<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseModel<UserProfileModel> responseModel;
                        try {
                            responseModel = JsonSerde.parseWithSingleContent(result, ResponseModel.class, UserProfileModel.class);
                            handler.onSuccess(responseModel.getData());
                        } catch (Exception ex) {
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                            handler.onFailure(App.getContext().getString(R.string.message_error_deserialization));
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        if (errorMessage.equals(App.getContext().getString(R.string.message_error_http_response_not_found))) {
                            handler.onSuccess(null);
                        } else {
                            mActions.onHttpFailure(handler, errorMessage);
                        }
                    }
                }
        );
    }
}
