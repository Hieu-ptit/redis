package com.viettel.coreapi.model.request;

import com.viettel.coreapi.validator.ValidPermission;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class CreateRoleRequest {
    @NotBlank
    private String role;
    @NotEmpty
    @ValidPermission
    private List<@Valid PermissionRequest> permissions;
}
