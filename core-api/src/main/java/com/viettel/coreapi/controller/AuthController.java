package com.viettel.coreapi.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.commons.thirdparty.api.request.AccountRequest;
import com.viettel.commons.thirdparty.api.request.EmployeeRequest;
import com.viettel.coreapi.model.request.LoginRequest;
import com.viettel.coreapi.model.response.LoginResponse;
import com.viettel.coreapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/accounts")
    public CompletableFuture<Response<Void>> create(@Valid @RequestBody AccountRequest request) {
        return authService.create(request)
                .thenApply(Response::ofSucceeded);
    }

    @PostMapping("/employee")
    public CompletableFuture<Response<Void>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        return authService.createEmployee(request)
                .thenApply(Response::ofSucceeded);
    }

    @PostMapping("/login")
    public CompletableFuture<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest)
                .thenApply(Response::ofSucceeded);
    }
}
