package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель профиля пользователя
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileModel {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Псевдоним или имя пользователя
     * */
    public String name;
    /**
     * Аватар пользователя в виде строки в формате base64,
     * полученной из файла изображения в формате png
     * */
    public String avatar;
    /**
     * Название родного языка пользователя
     * */
    public String nativeLanguageName;
    /**
     * Название языка, изучаемого пользователем
     * */
    public String learningLanguageName;
    /**
     * Название уровня владения языком, на который обучается пользователь
     * */
    public String levelName;

}
