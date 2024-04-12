package org.tyaa.training.client.android.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель пользователя
 * */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    /**
     * Логин
     * */
    public String name;
    /**
     * Пароль
     * */
    private String password;
    /**
     * Идентификатор роли
     * */
    public Long roleId;
    /**
     * Название роли
     * */
    public String roleName;
}
