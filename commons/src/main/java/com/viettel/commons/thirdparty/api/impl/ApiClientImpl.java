package com.viettel.commons.thirdparty.api.impl;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.runtime.Generics;
import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.model.response.Response;
import com.viettel.commons.thirdparty.api.ApiClient;
import com.viettel.commons.thirdparty.api.request.AccountRequest;
import com.viettel.commons.thirdparty.api.request.EmployeeRequest;
import com.viettel.commons.thirdparty.api.response.AccountResponse;
import com.viettel.commons.util.Constant;
import com.viettel.commons.util.ErrorCode;
import com.viettel.commons.util.Json;
import com.viettel.commons.util.RestClient;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class ApiClientImpl implements ApiClient {
    private static final JsonReader.ReadObject<Response<AccountResponse>> ACCOUNT_RESPONSE_READER =
            Json.findReader(Generics.makeParameterizedType(Response.class, AccountResponse.class));

    private static final JsonWriter.WriteObject<AccountRequest> CREATE_ACCOUNT_REQUEST_WRITER =
            Json.findWriter(AccountRequest.class);

    private static final JsonWriter.WriteObject<EmployeeRequest> CREATE_EMPLOYEE_REQUEST_WRITER =
            Json.findWriter(EmployeeRequest.class);
    private final String clientId;
    private final String clientSecret;
    private final String getAccountByEmailUrl;
    private final String getAccountByIdUrl;
    private final RestClient restClient;
    private final String createAccountUrl;

    private final String createEmployeeUrl;

    public ApiClientImpl(String clientId, String clientSecret, String coreBaseUrl, RestClient restClient) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.getAccountByEmailUrl = coreBaseUrl + "/accounts/m/";
        this.getAccountByIdUrl = coreBaseUrl + "/accounts/";;
        this.restClient = restClient;
        this.createAccountUrl = coreBaseUrl + "/accounts";
        this.createEmployeeUrl = coreBaseUrl + "/employees";
    }

    @Override
    public CompletableFuture<Void> create(AccountRequest request) {
        Map<String, String> headers = Map.of("Content-Type", "application/json",
                Constant.X_CLIENT_ID, clientId,
                Constant.X_CLIENT_SECRET, clientSecret);
        return restClient.post(URI.create(createAccountUrl), headers, request, CREATE_ACCOUNT_REQUEST_WRITER)
                .thenAccept(httpResponse -> {
                    if (httpResponse.statusCode() >= 400 && httpResponse.statusCode() < 500) {
                        log.error("can't call create account request, request {}, due: {}", () -> request, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists " + request.getEmail());
                    }
                    if (httpResponse.statusCode() >= 500) {
                        log.error("can't call create account request, request {}, due: {}", () -> request, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.INTERNAL_SERVICE_ERROR, "Internal service error");
                    }
                });
    }

    @Override
    public CompletableFuture<Void> createEmployee(EmployeeRequest request) {
        Map<String, String> headers = Map.of("Content-Type", "application/json",
                Constant.X_CLIENT_ID, clientId,
                Constant.X_CLIENT_SECRET, clientSecret);
        return restClient.post(URI.create(createEmployeeUrl), headers, request, CREATE_EMPLOYEE_REQUEST_WRITER)
                .thenAccept(httpResponse -> {
                    if (httpResponse.statusCode() >= 400 && httpResponse.statusCode() < 500) {
                        log.error("can't call create account request, request {}, due: {}", () -> request, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists ");
                    }
                    if (httpResponse.statusCode() >= 500) {
                        log.error("can't call create account request, request {}, due: {}", () -> request, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.INTERNAL_SERVICE_ERROR, "Internal service error");
                    }
                });
    }

    @Override
    public CompletableFuture<AccountResponse> getAccount(String email) {
        Map<String, String> headers = Map.of("Content-Type", "application/json",
                Constant.X_CLIENT_ID, clientId,
                Constant.X_CLIENT_SECRET, clientSecret);
        return restClient.get(URI.create(getAccountByEmailUrl + email), headers)
                .thenApply(httpResponse -> {
                    if (httpResponse.statusCode() == 200) {
                        return Json.decode(httpResponse.body(), ACCOUNT_RESPONSE_READER);
                    } else if (httpResponse.statusCode() >= 400 && httpResponse.statusCode() < 500) {
                        log.error("can't call get account request, email {}, due: {}", () -> email, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with email " + email);
                    } else {
                        log.error("can't call get account request, email {}, due: {}", () -> email, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.INTERNAL_SERVICE_ERROR, "Internal service error");
                    }
                })
                .thenApply(Response::getData);

    }

    @Override
    public CompletableFuture<AccountResponse> getAccount(Long accountId) {
        Map<String, String> headers = Map.of("Content-Type", "application/json",
                Constant.X_CLIENT_ID, clientId,
                Constant.X_CLIENT_SECRET, clientSecret);
        return restClient.get(URI.create(getAccountByIdUrl + accountId), headers)
                .thenApply(httpResponse -> {
                    if (httpResponse.statusCode() == 200) {
                        return Json.decode(httpResponse.body(), ACCOUNT_RESPONSE_READER);
                    } else if (httpResponse.statusCode() >= 400 && httpResponse.statusCode() < 500) {
                        log.error("can't call get account request, account id {}, due: {}", () -> accountId, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with email " + accountId);
                    } else {
                        log.error("can't call get account request, account id {}, due: {}", () -> accountId, () -> new String(httpResponse.body(), StandardCharsets.UTF_8));
                        throw new BusinessException(ErrorCode.INTERNAL_SERVICE_ERROR, "Internal service error");
                    }
                })
                .thenApply(Response::getData);

    }
}
