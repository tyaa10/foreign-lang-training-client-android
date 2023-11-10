package org.tyaa.training.client.android.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель роли пользователя
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel implements Serializable {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Название роли
     * */
    public String name;
}
