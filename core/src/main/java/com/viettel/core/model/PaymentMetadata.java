package com.viettel.core.model;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.experimental.Accessors;

@CompiledJson
@Data
@Accessors(chain = true)
public class PaymentMetadata implements TransactionMetadata {
    private String boxType;
    private String boxDescription;
    private String boxName;
}
