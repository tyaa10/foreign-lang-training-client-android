package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.LessonListItemModel;

import java.util.List;

/**
 * Абстракция службы уроков
 * */
public interface ILessonService {
    /**
     * Получить описания пунктов для списка уроков
     * (доступно только аутентифицированным)
     * */
    void getLessonListItems(IResultHandler<List<LessonListItemModel>> handler);
}
