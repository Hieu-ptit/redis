package com.viettel.commons.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class UpdatePasswordRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
