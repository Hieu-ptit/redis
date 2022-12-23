package com.viettel.core.service.internal;

import com.viettel.core.model.ClientKeysConfig;
import com.viettel.core.service.ConfigService;

public interface ConfigServiceInternal extends ConfigService {
    ClientKeysConfig getClientKeys();
}
