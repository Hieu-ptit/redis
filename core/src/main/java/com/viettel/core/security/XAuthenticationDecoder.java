package com.viettel.core.security;

import com.dslplatform.json.JsonReader;
import com.viettel.commons.model.WofMAccount;
import com.viettel.commons.util.Json;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class XAuthenticationDecoder {
    private static final JsonReader.ReadObject<WofMAccount> wofMAccount = Json.findReader(WofMAccount.class);

    public Authentication decode(String token) {
        byte[] decoded = Base64.getUrlDecoder().decode(token);
        var principal = Json.decode(decoded, wofMAccount);
        return new UsernamePasswordAuthenticationToken(principal, token, List.of());
    }
}
