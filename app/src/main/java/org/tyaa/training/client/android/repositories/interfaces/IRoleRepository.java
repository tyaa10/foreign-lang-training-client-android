package org.tyaa.training.client.android.repositories.interfaces;

import org.tyaa.training.client.android.handlers.IResultHandler;

/**
 * Абстракция репозитория ролей
 * */
public interface IRoleRepository {
    /**
     * Получить описание всех вариантов ролей пользователей в виде строки сырых данных
     * */
    void getRoles(IResultHandler<String> handler);
}
