package com.viettel.core.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.viettel.commons.util.Constant;
import com.viettel.core.exception.KeyNotFoundException;
import com.viettel.core.model.ClientKeysConfig;
import com.viettel.core.model.ConfigKey;
import com.viettel.core.model.ConfigValue;
import com.viettel.core.repository.ConfigRepository;
import com.viettel.core.service.internal.ConfigServiceInternal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ConfigServiceImpl implements ConfigServiceInternal {
    private static final int BILLION = 1000000;
    private static final KeyNotFoundException KEY_NOT_FOUND_EXCEPTION = new KeyNotFoundException("This config key not found");
    private final ConfigRepository configRepository;
    private final IMap<String, ConfigValue> configMap;
    private final long configCacheTtl;

    public ConfigServiceImpl(HazelcastInstance hazelcastInstance, ConfigRepository configRepository,
                             @Value("${app.config.cache.ttl:#{1800}}") long configCacheTtl) {
        this.configMap = hazelcastInstance.getMap(Constant.CONFIG);
        this.configRepository = configRepository;
        this.configCacheTtl = configCacheTtl;
    }

    @Override
    public void clearCache() {
        configMap.clear();
    }

    @Override
    public ClientKeysConfig getClientKeys() {
        return (ClientKeysConfig) getConfig(ConfigKey.CLIENT_KEYS);
    }


    private ConfigValue getConfig(String key) {
        var config = configMap.get(key);
        if (config == null) {
            var configOpt = configRepository.findByKey(key);
            if (configOpt.isEmpty()) {
                throw KEY_NOT_FOUND_EXCEPTION;
            }
            config = configOpt.get().getValue();
            configMap.putIfAbsent(key, config, configCacheTtl, TimeUnit.SECONDS);
        }
        return config;
    }

    private ConfigValue getConfigOrNull(String key) {
        var config = configMap.get(key);
        if (config == null) {
            var configOpt = configRepository.findByKey(key);
            if (configOpt.isEmpty()) {
                return null;
            }
            config = configOpt.get().getValue();
            configMap.putIfAbsent(key, config, configCacheTtl, TimeUnit.SECONDS);
        }
        return config;
    }
}
