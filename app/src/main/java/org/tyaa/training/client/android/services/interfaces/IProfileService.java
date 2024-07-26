package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;
import org.tyaa.training.client.android.models.UserProfileModel;

import java.util.List;

/**
 * Абстракция службы профилей пользователей
 * */
public interface IProfileService {
    /**
     * Получить данные профилей всех пользователей
     * (доступно только администраторам)
     * */
    void getProfiles(IResultHandler<List<UserProfileModel>> handler);
    /**
     * Создать профиль для текущего пользователя
     * */
    void createProfile(UserProfileModel profileModel, IResponseHandler handler);
}
