package org.tyaa.training.client.android.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.tyaa.training.client.android.App;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.models.WordTestModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.services.interfaces.IWordTestService;

import java.util.Objects;

/**
 * Реализация службы результатов проверки знания слов,
 * использующей сетевой источник данных, доступный по протоколу HTTP
 * */
public class HttpWordTestService extends BaseHttpService implements IWordTestService {

    @Override
    public void getWordTestResults(Long wordId, IResultHandler handler) {
        // запросить у сервера данные результатов проверки знания слова текущим пользователем,
        // указав id слова
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_tests_word_tests_words_results_uri)
                                .replace("{id}", String.valueOf(wordId))
                ),
                new IResultHandler<>() {
                    // если сервер дал положительный ответ
                    @Override
                    public void onSuccess(String result) {
                        try {
                            // попытаться десериализовать тело ответа сервера из Json-строки
                            // в Java-объект, поле data которого содержит Java-объект модели
                            // данных результатов проверки знания слова
                            ResponseModel<WordTestModel> responseModel =
                                    JsonSerde.parseWithSingleContent(result, ResponseModel.class, WordTestModel.class);
                            // если десериализация удалась - вызвать обработчик результата,
                            // переданный службе при вызове данного действия
                            handler.onSuccess(responseModel.getData());
                        } catch (JsonProcessingException ex) {
                            // иначе - вывести отладочные данные об исключении в консоль ОС
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

    @Override
    public void getWordStudyLessonTestResults(Long lessonId, IResultHandler<WordTestModel> handler) {
        // запросить у сервера данные результатов проверки знания слов урока текущим пользователем,
        // указав id урока
        mActions.doRequestForResult(
                String.format("%s/%s",
                        App.getContext().getString(R.string.network_base_server_url),
                        App.getContext().getString(R.string.network_tests_word_tests_lessons_results_uri)
                                .replace("{id}", String.valueOf(lessonId))
                ),
                new IResultHandler<>() {
                    // если сервер дал положительный ответ
                    @Override
                    public void onSuccess(String result) {
                        try {
                            // попытаться десериализовать тело ответа сервера из Json-строки
                            // в Java-объект, поле data которого содержит Java-объект модели
                            // данных результатов проверки знания слов урока
                            ResponseModel<WordTestModel> responseModel =
                                    JsonSerde.parseWithSingleContent(result, ResponseModel.class, WordTestModel.class);
                            // если десериализация удалась - вызвать обработчик результата,
                            // переданный службе при вызове данного действия
                            handler.onSuccess(responseModel.getData());
                        } catch (JsonProcessingException ex) {
                            // иначе - вывести отладочные данные об исключении в консоль ОС
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

    @Override
    public void addWordTestResult(Long wordId, Boolean success, IResponseHandler handler) {
        try {
            final String successJsonString = JsonSerde.serialize(success);
            mActions.doRequest(
                    String.format("%s/%s",
                            App.getContext().getString(R.string.network_base_server_url),
                            App.getContext().getString(R.string.network_tests_word_tests_words_results_add_uri)
                                    .replace("{id}", String.valueOf(wordId))
                    ),
                    successJsonString,
                    new IResponseHandler() {

                        @Override
                        public void onSuccess() {
                            handler.onSuccess();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            mActions.onHttpFailure(handler, errorMessage);
                        }
                    });
        } catch (JsonProcessingException e) {
            handler.onFailure(App.getContext().getString(R.string.message_error_serialization));
        }
    }
}
