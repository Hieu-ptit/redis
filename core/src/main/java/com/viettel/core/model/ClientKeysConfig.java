package com.viettel.core.model;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Map;

@CompiledJson(name = "client_keys")
@JsonTypeName("client_keys")
@Data
@Accessors(chain = true)
public class ClientKeysConfig implements ConfigValue, DataSerializable {
    private Map<String, String> value;

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(value);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        value = in.readObject();
    }
}
