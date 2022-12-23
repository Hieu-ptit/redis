package com.viettel.core.repository;

import com.viettel.core.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, InsertUpdateRepository<Employee>, CustomizedAccountRepository {

    @Query(value = "select nextval('employee_id_seq')", nativeQuery = true)
    long nextEmployeeId();

    @Transactional
    @Query(value = """
            INSERT INTO employee (id, name, metadata, company, boxes, created_at, updated_at)
            VALUES
            (:#{#account.id}, :#{#account.name}, :metadata, :company, :boxes, :#{#account.createdAt}, :#{#account.updatedAt}) returning *
            """, nativeQuery = true)
    Employee create(Employee account, String metadata, String boxes, String company);
}
