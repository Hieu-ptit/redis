package com.viettel.coreapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.viettel.commons.thirdparty.api.ApiClient;
import com.viettel.commons.thirdparty.api.impl.ApiClientImpl;
import com.viettel.commons.util.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.net.http.HttpClient;

@Configuration
public class AppConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }

    @Bean
    public RestClient restClient(HttpClient httpClient, ObjectMapper objectMapper) {
        return new RestClient(httpClient, objectMapper);
    }

//    @Bean
//    public AuthClient authClient(RestClient restClient, @Value("${tavern.auth-base-url}") String authBaseUrl) {
//        return new AuthClientImpl(restClient, authBaseUrl);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public ApiClient ApiClient(RestClient restClient,
                               @Value("${core.base-api-url}") String baseAccountUrl,
                               @Value("${core.client-id}") String clientId,
                               @Value("${core.client-secret}") String clientSecret) {
        return new ApiClientImpl(clientId, clientSecret, baseAccountUrl, restClient);
    }

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setHeaderPredicate(s -> true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(@Value("${hazelcast.cluster-name:#{null}}") String clusterName,
                                               @Value("${hazelcast.service-name:#{null}}") String serviceName,
                                               @Value("${hazelcast.namespace:#{null}}") String namespace) {
        final Config config = new Config();
        if (clusterName != null) {
            config.setClusterName(clusterName);
        }
        config.getCPSubsystemConfig().setCPMemberCount(0);
        if (namespace != null) {
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                    .setProperty("namespace", namespace)
                    .setProperty("service-name", serviceName);
        }
        return Hazelcast.newHazelcastInstance(config);
    }
}
