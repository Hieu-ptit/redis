package com.viettel.coreapi.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.thirdparty.api.ApiClient;
import com.viettel.commons.thirdparty.api.request.AccountRequest;
import com.viettel.commons.thirdparty.api.request.EmployeeRequest;
import com.viettel.commons.util.Constant;
import com.viettel.commons.util.ErrorCode;
import com.viettel.coreapi.model.request.LoginRequest;
import com.viettel.coreapi.model.response.LoginResponse;
import com.viettel.coreapi.security.JwtService;
import com.viettel.coreapi.service.AuthService;
import com.viettel.coreapi.util.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Service
public class AuthServiceImpl implements AuthService {
    private static final long JWT_TTL_SECONDS = 30 * 24 * 60 * 60L;
    private final ApiClient apiClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final IMap<Long, Boolean> idToValidAccount;

    public AuthServiceImpl(HazelcastInstance hazelcastInstance, ApiClient apiClient,
                           JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.apiClient = apiClient;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.idToValidAccount = hazelcastInstance.getMap(Constant.ID_TO_VALID_ACCOUNT);
    }

    @Override
    public CompletableFuture<Void> create(AccountRequest request) {
        return apiClient.getAccount(request.getEmail().trim())
                .thenAccept(accountResp -> {
                    if (accountResp != null) {
                        throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists, " + request.getEmail());
                    }
                })
                .exceptionallyCompose(t -> {
                    if (t instanceof CompletionException ce && ce.getCause() instanceof BusinessException be && be.getErrorCode() == ErrorCode.ACCOUNT_NOT_FOUND) {
//                        var password = PasswordGenerator.generate(12);
                        var hashPassword = passwordEncoder.encode(request.getPassword());
                        var activationCode = UUID.randomUUID().toString();
                        return apiClient.create(
                                new AccountRequest()
                                        .setPassword(hashPassword)
                                        .setActivationCode(activationCode)
                                        .setEmail(request.getEmail().trim())
                                        .setRole(request.getRole())
                                        .setName(request.getName()));
                    }
                    throw new CompletionException(t.getCause());
                });
    }

    @Override
    public CompletableFuture<LoginResponse> login(LoginRequest loginRequest) {
        return apiClient.getAccount(loginRequest.getEmail().trim())
                .thenApply(accResp -> {
                    if (!accResp.getActivated()) {
                        throw new BusinessException(ErrorCode.INVALID_ACCOUNT_ID, "Account not activated.");
                    }
                    if (!passwordEncoder.matches(loginRequest.getPassword(), accResp.getPassword())) {
                        throw new BusinessException(ErrorCode.WRONG_PASSWORD, "Wrong password");
                    }
                    var iatSeconds = System.currentTimeMillis() / 1000;
                    var expiredAt = iatSeconds + JWT_TTL_SECONDS;
                    Map<String, Object> claims = Map.of(
                            "id", accResp.getId(),
                            "email", accResp.getEmail(),
                            "iat", iatSeconds,
                            "exp", expiredAt);
                    var token = jwtService.generateToken(claims);
                    return new LoginResponse().setToken(token)
                            .setExpiredAt(expiredAt);
                });
    }

    @Override
    public CompletableFuture<Void> createEmployee(EmployeeRequest request) {
                        return apiClient.createEmployee(
                                new EmployeeRequest()
                                        .setBoxes(null)
                                        .setCompany(null)
                                        .setBoxId(request.getBoxId())
                                        .setName(request.getName()));
    }
}
