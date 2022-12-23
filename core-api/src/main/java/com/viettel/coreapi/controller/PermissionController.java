package com.viettel.coreapi.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.coreapi.model.request.CreateRoleRequest;
import com.viettel.coreapi.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/roles")
    public Response<Void> createRole(@RequestBody @Valid CreateRoleRequest request) {
        permissionService.createRole(request);
        return Response.ofSucceeded();
    }

}
