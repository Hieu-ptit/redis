package com.viettel.commons.thirdparty.api.request;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@CompiledJson
public class CompanyRequest {
    private Long id;
    private String name;
}
