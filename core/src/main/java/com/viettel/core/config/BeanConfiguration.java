package com.viettel.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.viettel.commons.util.RestClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.http.HttpClient;

@Configuration
public class BeanConfiguration {

    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.database}")
    private int db;
    @Value("${redis.connect-timeout}")
    private int timeout;

    @Bean
    public JedisPool create() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(30);
        poolConfig.setMaxIdle(30);
        if (StringUtils.isEmpty(password))
            password = null;

        return new JedisPool(poolConfig, host, port, timeout, password, db);
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
    public HttpClient httpClient() {
        return HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    @Bean
    public RestClient restClient(HttpClient httpClient, ObjectMapper objectMapper) {
        return new RestClient(httpClient, objectMapper);
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
