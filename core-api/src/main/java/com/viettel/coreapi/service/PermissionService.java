package com.viettel.coreapi.service;

import com.viettel.coreapi.model.request.CreateRoleRequest;

public interface PermissionService {
    void createRole(CreateRoleRequest request);
}
