package com.viettel.commons.thirdparty.api.request;


import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@CompiledJson
public class AccountRequest {
    private String email;
    private String password;
//    @JsonProperty("activation_code")
    private String activationCode;
    private String name;
    private String role;
}
