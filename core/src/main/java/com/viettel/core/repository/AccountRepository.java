package com.viettel.core.repository;

import com.viettel.core.model.entity.Account;
import com.viettel.core.model.request.AccountRequest;
import com.viettel.core.model.request.AccountUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, InsertUpdateRepository<Account>, JpaSpecificationExecutor<Account> {

    @Transactional
    @Query(value = """
            insert into account (name, email, password, deleted, activated, activation_code, created_at, updated_at)
            select
                :#{#request.name}, :#{#request.email.toLowerCase()}, :#{#request.password}, false, false, :#{#request.activationCode}, :now, :now
            where not exists (
                select 1 from account where email = :#{#request.email.toLowerCase()}
            ) returning *
            """, nativeQuery = true)
    Optional<Account> create(AccountRequest request, OffsetDateTime now);
    Optional<Account> findByIdAndDeleted(Long id, Boolean deleted);
    Optional<Account> findByIdAndActivatedAndDeleted(Long id, Boolean activated, Boolean deleted);
    Optional<Account> findByEmailAndDeleted(String email, Boolean deleted);

    @Transactional
    @Query(value = """
            update account set email = :#{#request.email}, name = :#{#request.name}
            where id = :id returning *
            """, nativeQuery = true)
    Optional<Account> update(AccountUpdateRequest request, Long id);
}
