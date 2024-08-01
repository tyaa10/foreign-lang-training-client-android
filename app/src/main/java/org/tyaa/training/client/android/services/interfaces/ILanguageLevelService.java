package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageLevelModel;

import java.util.List;

/**
 * Абстракция службы комбинаций "родной язык - изучаемый язык - уровень владения языком"
 * */
public interface ILanguageLevelService {
    /**
     * Получить список комбинаций
     * (доступно только аутентифицированным)
     * */
    void getLanguageLevels(IResultHandler<List<LanguageLevelModel>> handler);
}
