package com.viettel.core.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.core.model.request.CreateRoleRequest;
import com.viettel.core.model.response.PermissionResponse;
import com.viettel.core.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

    @GetMapping
    public Response<List<PermissionResponse>> getPermissions(@RequestParam @NotBlank String role) {
        return Response.ofSucceeded(permissionService.getPermissions(role));
    }
}
