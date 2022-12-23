package com.viettel.core.converter;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.viettel.commons.util.Json;
import com.viettel.core.model.BoxInvitationMetadata;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.nio.charset.StandardCharsets;

@Convert
public class RewardMetadataConverter implements AttributeConverter<BoxInvitationMetadata, String> {
    private static final JsonReader.ReadObject<BoxInvitationMetadata> metadataReader = Json.findReader(BoxInvitationMetadata.class);
    private static final JsonWriter.WriteObject<BoxInvitationMetadata> metadataWriter = Json.findWriter(BoxInvitationMetadata.class);

    @Override
    public String convertToDatabaseColumn(BoxInvitationMetadata metadata) {
        return metadata != null ? Json.encodeToString(metadata, metadataWriter) : null;
    }

    @Override
    public BoxInvitationMetadata convertToEntityAttribute(String dbData) {
        return dbData != null ? Json.decode(dbData.getBytes(StandardCharsets.UTF_8), metadataReader) : null;
    }
}
