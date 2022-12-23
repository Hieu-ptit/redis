package com.viettel.core.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class AccountDetailResponse {
    private Long id;
    private List<String> roles;
    private String email;
    private String name;
    private Boolean activated;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
