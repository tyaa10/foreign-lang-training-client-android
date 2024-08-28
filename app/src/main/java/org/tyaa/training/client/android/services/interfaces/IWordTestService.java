package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.JsonPatchModel;
import org.tyaa.training.client.android.models.WordTestModel;

import java.util.List;

/**
 * Абстракция службы результатов проверки знания слов
 * (доступно только аутентифицированным)
 * */
public interface IWordTestService {
    /**
     * Получить данные результатов проверки знания слова
     * @param wordId идентификатор слова, результаты проверки знаний которого нужно получить
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    void getWordTestResults(Long wordId, IResultHandler handler);
    /**
     * Получить данные результатов проверки знания слов указанного урока
     * @param lessonId идентификатор урока по изучению слов, результаты проверки знаний которого нужно получить
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    void getWordStudyLessonTestResults(Long lessonId, IResultHandler<WordTestModel> handler);
    /**
     * Создать запись результатов проверки знания слова
     * @param wordId идентификатор слова, результаты проверки знаний которого нужно получить
     * @param wordTestResults начальные значения результатов проверки знаний слова
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    // void createWordTestResults(Long wordId, WordTestModel wordTestResults, IResponseHandler handler);
    /**
     * Обновить данные результатов проверки знания слова
     * @param id идентификатор результатов проверки знаний, которые нужно изменить
     * @param patches новые значения результатов проверки знаний слова
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    // void updateWordTestResults(Long id, List<JsonPatchModel> patches, IResponseHandler handler);
    /**
     * Прибавить новый результат к результатам проверки знания слова
     * @param wordId идентификатор слова, к результатам проверки знаний которого нужно прибавить новый результат
     * @param success успешен ли был перевод слова при данной попытке
     * @param handler обработчик результата выполнения действия службы, задаваемый клиентом службы
     * */
    void addWordTestResult(Long wordId, Boolean success, IResponseHandler handler);
}
