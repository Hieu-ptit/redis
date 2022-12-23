package com.viettel.coreapi.service;

import com.viettel.commons.thirdparty.api.request.AccountRequest;
import com.viettel.commons.thirdparty.api.request.EmployeeRequest;
import com.viettel.coreapi.model.request.LoginRequest;
import com.viettel.coreapi.model.response.LoginResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface AuthService {
    CompletableFuture<Void> create(AccountRequest request);
    CompletableFuture<LoginResponse> login(LoginRequest loginRequest);

    CompletableFuture<Void> createEmployee(EmployeeRequest request);
}
