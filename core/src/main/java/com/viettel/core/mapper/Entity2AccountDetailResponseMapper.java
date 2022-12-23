package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Account;
import com.viettel.core.model.response.AccountDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Entity2AccountDetailResponseMapper extends BeanMapper<Account, AccountDetailResponse> {
    Entity2AccountDetailResponseMapper INSTANCE = Mappers.getMapper(Entity2AccountDetailResponseMapper.class);
}
