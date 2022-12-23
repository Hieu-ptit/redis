package com.viettel.core.model;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@CompiledJson
@Data
@Accessors(chain = true)
public class BoxInvitationMetadata {
    private Long boxId;
    private String name;
}
