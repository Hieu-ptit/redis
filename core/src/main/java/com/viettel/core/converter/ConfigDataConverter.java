package com.viettel.core.converter;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.viettel.commons.util.Json;
import com.viettel.core.model.ConfigValue;

import javax.persistence.AttributeConverter;
import java.nio.charset.StandardCharsets;

public class ConfigDataConverter implements AttributeConverter<ConfigValue, String> {
    private static final JsonReader.ReadObject<ConfigValue> dataReader = Json.findReader(ConfigValue.class);
    private static final JsonWriter.WriteObject<ConfigValue> dataWriter = Json.findWriter(ConfigValue.class);

    @Override
    public String convertToDatabaseColumn(ConfigValue metadata) {
        return metadata != null ? Json.encodeToString(metadata, dataWriter) : null;
    }

    @Override
    public ConfigValue convertToEntityAttribute(String dbData) {
        return dbData != null ? Json.decode(dbData.getBytes(StandardCharsets.UTF_8), dataReader) : null;
    }
}
