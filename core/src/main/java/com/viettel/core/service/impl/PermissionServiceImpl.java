package com.viettel.core.service.impl;

import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.util.ErrorCode;
import com.viettel.core.model.request.CreateRoleRequest;
import com.viettel.core.model.request.PermissionRequest;
import com.viettel.core.model.response.PermissionResponse;
import com.viettel.core.service.PermissionService;
import com.viettel.core.service.internal.PermissionServiceInternal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PermissionServiceImpl implements PermissionServiceInternal {

    private final Enforcer enforcer;

    @Override
    public void createRole(CreateRoleRequest request) {
        var permissions = enforcer.getPermissionsForUser(request.getRole());
        if (!permissions.isEmpty()) {
            throw new BusinessException(ErrorCode.ROLE_ALREADY_EXISTS, "Role " + request.getRole() + " already exists");
        }
        savePolicies(request.getRole(), request.getPermissions());
    }

    @Override
    public List<PermissionResponse> getPermissions(String role) {
        var permissions = enforcer.getPermissionsForUser(role);
        return permissions.stream()
                .map(permission -> new PermissionResponse().setTarget(permission.get(1)).setAction(permission.get(2)))
                .toList();
    }

    @Override
    public List<String> getRoles(Long accountId) {
        enforcer.loadPolicy();
        return enforcer.getRolesForUser(accountId.toString());
    }

    @Override
    public void assign(Long accountId, String role) {
        if (enforcer.getPermissionsForUser(role).isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_ROLE, "Role not exists, " + role);
        }
        enforcer.addRoleForUser(accountId.toString(), role);
        enforcer.savePolicy();
    }


    private void savePolicies(String role, List<PermissionRequest> permissions) {
        var rules = permissions.stream().map(permission -> List.of(role, permission.getTarget(), permission.getAction())).toList();
        enforcer.addPolicies(rules);
        enforcer.savePolicy();
    }
}
