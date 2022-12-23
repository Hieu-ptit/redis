package com.viettel.core.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RewardBoxRequest {

    private Long id;
    private String name;
    private String description;
    private String image;
}
