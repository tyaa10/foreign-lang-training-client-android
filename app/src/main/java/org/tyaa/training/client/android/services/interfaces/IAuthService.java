package org.tyaa.training.client.android.services.interfaces;

import org.tyaa.training.client.android.handlers.IResponseHandler;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.models.UserModel;

import java.util.List;

/**
 * Абстракция службы безопасности
 * */
public interface IAuthService {
    /**
     * Получить описание всех вариантов ролей пользователей в виде строки сырых данных
     * */
    void getRoles(IResultHandler<List<RoleModel>> handler);
    /**
     * Узнать, аутентифицирован ли пользователь
     * */
    void checkUser(IResultHandler<UserModel> handler);
    /**
     * Войти в учётную запись по логину и паролю
     * */
    void signIn(String login, String password, IResponseHandler handler);
    /**
     * Зарегистрировать новую учётную запись
     * */
    void signUp(String login, String password, IResponseHandler handler);
    /**
     * Выйти из учётной записи
     * */
    void signOut(IResponseHandler handler);
}
