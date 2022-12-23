package com.viettel.core.service;

import com.viettel.core.model.request.CreateRoleRequest;
import com.viettel.core.model.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    void createRole(CreateRoleRequest request);
    List<PermissionResponse> getPermissions(String role);
}
