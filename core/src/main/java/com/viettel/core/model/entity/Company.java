package com.viettel.core.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
public class Company extends BaseEntity<Company> {

    @Id
    @SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
    Long id;
    String name;
    @Override
    protected Company self() {
        return this;
    }
}
