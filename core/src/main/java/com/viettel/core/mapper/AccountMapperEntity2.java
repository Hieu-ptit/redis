package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Employee;
import com.viettel.core.model.request.EmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapperEntity2 extends BeanMapper<EmployeeRequest, Employee> {
    AccountMapperEntity2 INSTANCE = Mappers.getMapper(AccountMapperEntity2.class);
}
