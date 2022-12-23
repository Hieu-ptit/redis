package com.viettel.coreapi.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.viettel.commons.model.WofMAccount;
import com.viettel.commons.thirdparty.api.ApiClient;
import com.viettel.commons.util.Constant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_START_INDEX = BEARER_TOKEN_PREFIX.length();
    private final JwtService jwtService;
    private final IMap<Long, Boolean> idToValidAccount;
    private final ApiClient apiClient;
    private final Long defaultValidAccountCacheTtl;
    private final IMap<String, Integer> tokenRevokedMap;

    public JwtAuthenticationFilter(HazelcastInstance hazelcastInstance, JwtService jwtService, ApiClient apiClient,
                                   @Value("${castle.account.cache.ttl:#{1800}}") Long defaultValidAccountCacheTtl) {
        this.jwtService = jwtService;
        this.apiClient = apiClient;
        this.idToValidAccount = hazelcastInstance.getMap(Constant.ID_TO_VALID_ACCOUNT);
        this.tokenRevokedMap = hazelcastInstance.getMap(Constant.TOKEN_REVOKED_MAP);
        this.defaultValidAccountCacheTtl = defaultValidAccountCacheTtl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        final var token = header.substring(TOKEN_START_INDEX);
        var authentication = jwtService.decode(token);
        if (authentication != null) {
            var jti = ((DecodedJWT) authentication.getDetails()).getId();
            var wofMAccount = (WofMAccount) authentication.getPrincipal();
            if (!isValidToken(jti) || !isValidAccount(wofMAccount.getId())) {
                filterChain.doFilter(request, response);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Boolean isValidToken(String jti) {
       return tokenRevokedMap.get(jti) == null;
    }

    private Boolean isValidAccount(Long id) {
        var valid = idToValidAccount.get(id);
        if (valid == null) {
            try {
                var account = apiClient.getAccount(id).join();
                valid = account.getActivated();
                idToValidAccount.put(id, account.getActivated(), defaultValidAccountCacheTtl, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error(e);
                valid = false;
                idToValidAccount.put(id, false, defaultValidAccountCacheTtl, TimeUnit.SECONDS);
            }
        }
        return valid;
    }

}
