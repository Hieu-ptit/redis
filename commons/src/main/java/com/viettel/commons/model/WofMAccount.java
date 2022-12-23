package com.viettel.commons.model;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@CompiledJson
@Data
@Accessors(chain = true)
public class WofMAccount {
    private Long id;
    private String email;
    private String jti;
    private Long iat;
    private Long exp;
    @JsonIgnore
    private String authorization;

}
