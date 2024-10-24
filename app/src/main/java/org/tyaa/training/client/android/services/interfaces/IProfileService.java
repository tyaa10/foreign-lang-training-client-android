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
     * и получить обратно данные профиля текущего пользователя,
     * дополненные сервером (добавлен идентификатор)
     * */
    void createProfile(UserProfileModel profileModel, IResultHandler<UserProfileModel> handler);
    /**
     * Получить данные профиля текущего пользователя
     * (доступно только аутентифицированным)
     * */
    void getCurrentUserProfile(IResultHandler<UserProfileModel> handler);
}
