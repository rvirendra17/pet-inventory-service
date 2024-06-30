package com.abc.xyz.PetStore.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {
        "status",
        "code",
        "message",
        "type",
        "transactionId",
        "path"

})
public class ErrorResponse {

    private String status;
    private int code;
    private String message;
    private String type;

    private String transactionId;
    private String path;

}
