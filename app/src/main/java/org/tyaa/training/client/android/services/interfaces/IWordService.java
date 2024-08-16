package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.WordModel;

import java.util.List;

/**
 * Абстракция службы данных изучаемых слов
 * */
public interface IWordService {
    /**
     * Получить данные изучаемых слов
     * (доступно только аутентифицированным)
     * */
    void getWords(Long lessonId, IResponseHandler handler);
}
