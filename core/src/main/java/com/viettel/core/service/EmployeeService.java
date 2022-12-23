package com.viettel.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.request.EmployeeRequest;
import com.viettel.core.model.response.EmployeeResponse;

public interface EmployeeService {

    long nextAccountId();

    void create(EmployeeRequest request) throws JsonProcessingException;

    EmployeeResponse getEmployee(Long id);
}
