package org.tyaa.training.client.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Модель тела ответа сервера
 * @param <T> тип полезной нагрузки
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {
    public static final String SUCCESS_STATUS = "success";
    public static final String FAIL_STATUS = "fail";
    private String status;
    private String message;
    private T data;
}
