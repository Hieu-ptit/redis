package com.viettel.core.repository;

import com.dslplatform.json.JsonWriter;
import com.viettel.commons.util.Json;
import com.viettel.core.model.BoxInvitationMetadata;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.entity.Company;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeBatchInsertSetter implements BatchPreparedStatementSetter {

    private final Employee employee;

    private static final JsonWriter.WriteObject<BoxInvitationMetadata> REWARD_METADATA_WRITE_OBJECT = Json.findWriter(BoxInvitationMetadata.class);

    private static final JsonWriter.WriteObject<Company> COMPANY_WRITE_OBJECT = Json.findWriter(Company.class);


    public EmployeeBatchInsertSetter(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setLong(1, employee.getId());
        ps.setString(2, employee.getName());
        ps.setObject(3, Json.encodeToString(employee.getMetadata(), REWARD_METADATA_WRITE_OBJECT));
        ps.setObject(4, employee.getCreatedAt());
        ps.setObject(5, employee.getUpdatedAt());
    }

    @Override
    public int getBatchSize() {
        return 1;
    }
}
