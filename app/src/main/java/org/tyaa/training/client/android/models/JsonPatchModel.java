package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Схема тела HTTP-запроса, соответствующего стандарту "JSON Patch"
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonPatchModel {

    /**
     * Название действия (одно из стандартного набора {@link JsonPatchModel.Op})
     * */
    private Op op;

    public enum Op {
        replace, add, remove, copy, move, test
    }

    /**
     * Путь к изменяемому полю модели данных
     * */
    private String path;

    /**
     * Новое значение для изменения значения поля
     * */
    private String value;
}
