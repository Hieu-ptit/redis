package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Account;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.response.AccountDetailResponse;
import com.viettel.core.model.response.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Entity2EmployeeResponseMapper extends BeanMapper<Employee, EmployeeResponse> {
    Entity2EmployeeResponseMapper INSTANCE = Mappers.getMapper(Entity2EmployeeResponseMapper.class);
}
