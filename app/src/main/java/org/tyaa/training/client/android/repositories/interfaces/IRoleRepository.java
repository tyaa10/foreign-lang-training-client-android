package org.tyaa.training.client.android.repositories.interfaces;

import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;

import java.util.List;

/**
 * Абстракция репозитория ролей
 * */
public interface IRoleRepository {
    /**
     * Получить описание всех вариантов ролей пользователей в виде строки сырых данных
     * */
    void getRoles(IResultHandler<List<RoleModel>> handler);
}
