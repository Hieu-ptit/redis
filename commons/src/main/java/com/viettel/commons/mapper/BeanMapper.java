package com.viettel.commons.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface BeanMapper<T, R> {
    R map(T source);

    List<R> mapList(List<T> source);

    void mapTo(T source, @MappingTarget R target);
}
