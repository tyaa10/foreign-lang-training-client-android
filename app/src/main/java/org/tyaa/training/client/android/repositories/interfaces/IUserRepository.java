package org.tyaa.training.client.android.repositories.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.UserModel;

/**
 * Абстракция репозитория пользователей
 * */
public interface IUserRepository {
    /**
     * Узнать, аутентифицирован ли пользователь
     * */
    void checkUser(IResultHandler<UserModel> handler);
}
