package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Company;
import com.viettel.core.model.request.CompanyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapperEntity2 extends BeanMapper<CompanyRequest, Company> {
    CompanyMapperEntity2 INSTANCE = Mappers.getMapper(CompanyMapperEntity2.class);
}
