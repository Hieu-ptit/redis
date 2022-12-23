package com.viettel.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientKeysConfig.class, name = "client_key")
})
public interface ConfigValue {
}
