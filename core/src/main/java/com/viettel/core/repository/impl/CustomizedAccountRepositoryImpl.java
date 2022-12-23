package com.viettel.core.repository.impl;

import com.viettel.core.model.entity.Employee;
import com.viettel.core.repository.EmployeeBatchInsertSetter;
import com.viettel.core.repository.CustomizedAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CustomizedAccountRepositoryImpl implements CustomizedAccountRepository {

    private static final String INSERT_ACTION_PERMIT_SQL = """
            INSERT INTO account (id, name, metadata, created_at, updated_at) VALUES (?, ?, ?, ?,?)
            ON CONFLICT (account_id, action) DO NOTHING
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int[] create(Employee account) {
        return jdbcTemplate.batchUpdate(INSERT_ACTION_PERMIT_SQL, new EmployeeBatchInsertSetter(account));
    }
}
