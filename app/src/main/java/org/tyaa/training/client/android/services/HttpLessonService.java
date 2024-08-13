package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LessonListItemModel;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.ILessonService;

import java.util.List;

/**
 * Реализация службы уроков,
 * использующей сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpLessonService extends BaseHttpService implements ILessonService {

    @Override
    public void getLessonListItems(IResultHandler<List<LessonListItemModel>> handler) {

        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_lessons_list_items_uri)
                ),
                new IResultHandler<String>() {

                    @Override
                    public void onSuccess(String result) {
                        ResponseModel<List<LessonListItemModel>>  responseModel;
                        try {
                            responseModel =
                                    JsonSerde.parseWithListContent(
                                            result,
                                            ResponseModel.class,
                                            LessonListItemModel.class
                                    );
                            handler.onSuccess(responseModel.getData());
                        } catch (JsonProcessingException ex) {
                            if (ex.getMessage() != null)
                                Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), ex.getMessage());
                            Log.println(Log.ERROR, "StackTrace", Log.getStackTraceString(ex));
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
