package com.viettel.core.model;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@CompiledJson
public class RewardBox {
    private Long id;
    private String name;
    private String description;
    private String image;
}
