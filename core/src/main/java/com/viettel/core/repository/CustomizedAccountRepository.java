package com.viettel.core.repository;

import com.viettel.core.model.entity.Employee;

public interface CustomizedAccountRepository {
    int[] create(Employee account);
}
