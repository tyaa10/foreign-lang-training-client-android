package org.tyaa.training.client.android.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    public static final String SUCCESS_STATUS = "success";
    public static final String FAIL_STATUS = "fail";
    private String status;
    private String message;
    private Object data;
}
