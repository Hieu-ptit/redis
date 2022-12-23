package com.viettel.core.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class PermissionRequest {
    @NotBlank
    private String target;
    @NotBlank
    private String action;
}
