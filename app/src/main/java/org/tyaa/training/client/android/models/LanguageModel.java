package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель языка
 * */
public class LanguageModel {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Название языка
     * */
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
