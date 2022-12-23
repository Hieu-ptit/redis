package com.viettel.core.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EmployeeRequest {

    private String name;

    private CompanyRequest company;

    private Long boxId;

    private String nameBox;

    private List<RewardBoxRequest> boxes;
}
