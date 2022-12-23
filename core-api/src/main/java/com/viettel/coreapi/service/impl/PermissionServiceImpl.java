package com.viettel.coreapi.service.impl;

import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.util.ErrorCode;
import com.viettel.coreapi.model.request.CreateRoleRequest;
import com.viettel.coreapi.model.request.PermissionRequest;
import com.viettel.coreapi.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PermissionServiceImpl implements PermissionService {

    private final Enforcer enforcer;

    @Override
    public void createRole(CreateRoleRequest request) {
        var permissions = enforcer.getPermissionsForUser(request.getRole());
        if (!permissions.isEmpty()) {
            throw new BusinessException(ErrorCode.ROLE_ALREADY_EXISTS, "Role " + request.getRole() + " already exists");
        }
        savePolicies(request.getRole(), request.getPermissions());
    }

    private void savePolicies(String role, List<PermissionRequest> permissions) {
        var rules = permissions.stream().map(permission -> List.of(role, permission.getTarget(), permission.getAction())).toList();
        enforcer.addPolicies(rules);
        enforcer.savePolicy();
    }
}
