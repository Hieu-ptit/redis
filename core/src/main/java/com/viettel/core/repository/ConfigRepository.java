package com.viettel.core.repository;

import com.viettel.core.model.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>, InsertUpdateRepository<Config>, JpaSpecificationExecutor<Config> {
    @Query(value = "select * from config where key = :key FOR UPDATE", nativeQuery = true)
    Optional<Config> findByKey(String key);

}
