package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;

/**
 * Абстракция службы данных изучаемых слов
 * */
public interface IWordService {
    /**
     * Получить данные изучаемых слов
     * (доступно только аутентифицированным)
     * @param lessonId идентификатор урока по изучению слов, слова которого нужно получить
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    void getWords(Long lessonId, IResponseHandler handler);
}
