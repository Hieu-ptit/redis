package com.viettel.core.model.response;

import com.viettel.core.model.BoxInvitationMetadata;
import com.viettel.core.model.RewardBox;
import com.viettel.core.model.entity.Company;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class EmployeeResponse {

    private Long id;
    private String name;
    private List<RewardBox> boxes;
    private BoxInvitationMetadata metadata;
    private Company company;
}
