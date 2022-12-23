package com.viettel.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

public class InsertUpdateRepositoryImpl<T> implements InsertUpdateRepository<T> {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public <S extends T> S insert(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public <S extends T> S update(S entity) {
        return entityManager.merge(entity);
    }

    @Transactional
    @Override
    public <S extends T> List<S> insertAll(List<S> entities) {
        entities.forEach(entityManager::persist);
        return entities;
    }

    @Transactional
    @Override
    public <S extends T> List<S> updateAll(List<S> entities) {
        return entities.stream().map(entityManager::merge).toList();
    }

}
