package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapperEntity extends BeanMapper<AccountRequest, AccountResponse> {
    AccountMapperEntity INSTANCE = Mappers.getMapper(AccountMapperEntity.class);
}
