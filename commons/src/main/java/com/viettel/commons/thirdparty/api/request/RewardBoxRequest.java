package com.viettel.commons.thirdparty.api.request;

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
