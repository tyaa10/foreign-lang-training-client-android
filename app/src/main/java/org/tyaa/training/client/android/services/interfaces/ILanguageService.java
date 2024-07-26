package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LanguageModel;

import java.util.List;

/**
 * Абстракция службы языков
 * */
public interface ILanguageService {
    /**
     * Получить список языков
     * (доступно только аутентифицированным)
     * */
    void getLanguages(IResultHandler<List<LanguageModel>> handler);
}
