package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.RewardBox;
import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.request.RewardBoxRequest;
import com.viettel.core.model.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RewardBoxMapperEntity extends BeanMapper<RewardBoxRequest, RewardBox> {
    RewardBoxMapperEntity INSTANCE = Mappers.getMapper(RewardBoxMapperEntity.class);
}
