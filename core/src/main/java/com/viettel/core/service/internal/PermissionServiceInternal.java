package com.viettel.core.service.internal;

import com.viettel.core.service.PermissionService;

import java.util.List;

public interface PermissionServiceInternal extends PermissionService {
    void assign(Long accountId, String role);

    List<String> getRoles(Long accountId);
}
