package com.viettel.core.service;

import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.request.AccountUpdateRequest;
import com.viettel.core.model.response.AccountDetailResponse;
import com.viettel.core.model.response.AccountResponse;

public interface AccountService {
    AccountResponse getAccount(String email);
    AccountResponse getAccount(Long accountId);
    void create(AccountRequest request);

    AccountDetailResponse getAccountDetail(Long accountId);

    void update(Long id, AccountUpdateRequest request);
}
