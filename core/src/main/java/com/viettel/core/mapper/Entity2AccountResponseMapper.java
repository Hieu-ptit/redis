package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Account;
import com.viettel.core.model.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Entity2AccountResponseMapper extends BeanMapper<Account, AccountResponse> {
    Entity2AccountResponseMapper INSTANCE = Mappers.getMapper(Entity2AccountResponseMapper.class);
}
