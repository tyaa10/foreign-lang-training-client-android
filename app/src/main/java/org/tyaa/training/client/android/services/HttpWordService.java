package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.models.WordModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.IWordService;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.state.interfaces.IState;

import java.util.List;
import java.util.Objects;

/**
 * Реализация службы слов,
 * использующей сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpWordService extends BaseHttpService implements IWordService {

    private final IState mState;

    public HttpWordService() {
        super();
        this.mState = new InMemoryLocalState();
    }

    @Override
    public void getWords(Long lessonId, IResponseHandler handler) {

        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_lessons_word_lesson_words_uri)
                                .replace("{id}", String.valueOf(lessonId))
                ),
                new IResultHandler<>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            ResponseModel<List<WordModel>> responseModel =
                                    JsonSerde.parseWithListContent(result, ResponseModel.class, WordModel.class);
                            // Если десериализация выполнилась без выброса исключения:
                            // 1. добавить в объект текущего состояния приложения список моделей слов
                            mState.addCurrentLessonWords(responseModel.getData());
                            // 2. вызвать обработчик ответа об успешном выполнении действия
                            handler.onSuccess();
                        } catch (JsonProcessingException ex) {
                            // Иначе - вывести отладочные данные об исключении в консоль ОС
                            Log.println(Log.ERROR, App.getContext().getString(R.string.message_error_deserialization), Objects.requireNonNull(ex.getMessage()));
                            // и вызвать обработчик провала десериализации
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
