package com.viettel.core.service.impl;

import com.viettel.commons.exception.BusinessException;
import com.viettel.commons.util.ErrorCode;
import com.viettel.core.mapper.Entity2AccountDetailResponseMapper;
import com.viettel.core.mapper.Entity2AccountResponseMapper;
import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.request.AccountUpdateRequest;
import com.viettel.core.model.response.AccountDetailResponse;
import com.viettel.core.model.response.AccountResponse;
import com.viettel.core.repository.AccountRepository;
import com.viettel.core.service.AccountService;
import com.viettel.core.service.internal.PermissionServiceInternal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final PermissionServiceInternal permissionService;

    @Override
    public AccountResponse getAccount(String email) {
        var account = repository.findByEmailAndDeleted(email, false)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with email " + email));
        return Entity2AccountResponseMapper.INSTANCE.map(account);
    }

    @Override
    public AccountResponse getAccount(Long accountId) {
        var account = repository.findByIdAndActivatedAndDeleted(accountId, true, false)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with id " + accountId));
        return Entity2AccountResponseMapper.INSTANCE.map(account);
    }

    @Override
    @Transactional
    public void create(AccountRequest request) {
        var account = repository.create(request, OffsetDateTime.now())
                .orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists, " + request.getEmail()));
        permissionService.assign(account.getId(), request.getRole());
    }

    @Override
    public AccountDetailResponse getAccountDetail(Long accountId) {
        var account = repository.findByIdAndDeleted(accountId, false)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with id " + accountId));
        var roles = permissionService.getRoles(accountId);
        return Entity2AccountDetailResponseMapper.INSTANCE.map(account)
                .setRoles(roles);
    }

    @Override
    public void update(Long id, AccountUpdateRequest request) {
        var account = repository.findByIdAndDeleted(id, false).orElseThrow(
                () -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, ErrorCode.ACCOUNT_NOT_FOUND.getMessage()));
        repository.update(request, id);
    }
}
