package com.viettel.core.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class AccountUpdateRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
