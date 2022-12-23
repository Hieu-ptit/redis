package com.viettel.commons.thirdparty.api;

import com.viettel.commons.thirdparty.api.request.AccountRequest;
import com.viettel.commons.thirdparty.api.request.EmployeeRequest;
import com.viettel.commons.thirdparty.api.response.AccountResponse;

import java.util.concurrent.CompletableFuture;

public interface ApiClient {
    CompletableFuture<Void> create(AccountRequest request);

    CompletableFuture<Void> createEmployee(EmployeeRequest request);

    CompletableFuture<AccountResponse> getAccount(String email);

    CompletableFuture<AccountResponse> getAccount(Long accountId);
}
