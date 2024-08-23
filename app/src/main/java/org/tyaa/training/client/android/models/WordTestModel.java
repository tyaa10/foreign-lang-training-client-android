package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель результатов проверок знания слова
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordTestModel {

    /**
     * Локально уникальный идентификатор
     * */
    public Long id;

    /**
     * Число попыток перевода слова
     * */
    public Integer attemptsNumber;

    /**
     * Число успешных случаев перевода слова
     * */
    public Integer successNumber;
}
