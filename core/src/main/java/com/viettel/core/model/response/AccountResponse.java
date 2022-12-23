package com.viettel.core.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class AccountResponse {
    private Long id;
    private String email;
    private String name;
    private String password;
    private Boolean activated;
    private List<String> roles;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
