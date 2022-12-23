package com.viettel.core.config;

import com.viettel.commons.model.WofMAccount;
import com.viettel.core.service.ConfigService;
import com.viettel.core.service.internal.ConfigServiceInternal;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CustomizedPermissionEvaluator implements PermissionEvaluator {
    private final Enforcer enforcer;

    private final ConfigServiceInternal configService;

    public CustomizedPermissionEvaluator(Enforcer enforcer, ConfigServiceInternal configService) {
        this.enforcer = enforcer;
        this.configService = configService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        var account = (WofMAccount) authentication.getPrincipal();
        return hasPrivilege(account.getId().toString(), targetDomainObject + "", permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ("CLIENT".equals(targetType)) {
            String[] apiKey = ((String) targetId).split(":");
            return isValid(apiKey[0], apiKey[1]) && hasPrivilege(apiKey[0], "*", permission);
        }
        return false;
    }

    private boolean isValid(String clientId, String clientSecret) {
        var clientKeys = configService.getClientKeys().getValue();
        return clientSecret.equals(clientKeys.get(clientId));
    }

    private boolean hasPrivilege(Object sub, Object obj, Object permission) {
        return enforcer.enforce(sub, obj, permission);
    }
}
