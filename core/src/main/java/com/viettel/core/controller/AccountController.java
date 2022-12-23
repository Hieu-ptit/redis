package com.viettel.core.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.request.AccountUpdateRequest;
import com.viettel.core.model.response.AccountDetailResponse;
import com.viettel.core.model.response.AccountResponse;
import com.viettel.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public Response<AccountResponse> getAccount(@Positive @PathVariable("id") Long accountId) {
        return Response.ofSucceeded(accountService.getAccount(accountId));
    }

    @GetMapping("/m/{email}")
    public Response<AccountResponse> getAccount(@NotBlank @PathVariable("email") String email) {
        return Response.ofSucceeded(accountService.getAccount(email));
    }

    @GetMapping("/details/{id}")
    public Response<AccountDetailResponse> getAccountDetail(@Positive @PathVariable("id") Long accountId) {
        return Response.ofSucceeded(accountService.getAccountDetail(accountId));
    }

    @PostMapping
    public Response<Void> create(@Valid @RequestBody AccountRequest request) {
        accountService.create(request);
        return Response.ofSucceeded();
    }

    @PutMapping("/{id}")
    public Response<Void> update(@PathVariable("id") Long id,
                                 @Valid @RequestBody AccountUpdateRequest request) {
        accountService.update(id, request);
        return Response.ofSucceeded();
    }
}
