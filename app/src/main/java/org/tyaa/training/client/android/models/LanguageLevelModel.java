package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель комбинации "родной язык - изучаемый язык - уровень владения языком"
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LanguageLevelModel {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Описание родного языка пользователя
     * */
    public LanguageModel nativeLanguage;
    /**
     * Описание языка, изучаемого пользователем
     * */
    public LanguageModel learningLanguage;
    /**
     * Описание уровня владения языком, на который обучается пользователь
     * */
    public LevelModel level;
    /**
     * Активна ли данная комбинация
     * */
    public Boolean active;
}
