package com.viettel.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "$type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BoxInvitationMetadata.class, name = "box_invitation"),
})
public interface RewardMetadata {
}
