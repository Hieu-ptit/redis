package com.viettel.core.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermissionResponse {
    private String action;
    private String target;
}
