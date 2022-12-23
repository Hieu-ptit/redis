package com.viettel.core.model.entity;

import com.dslplatform.json.CompiledJson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@CompiledJson
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
@Audited
public class Account extends BaseEntity<Account>{

    @Id
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    Long id;
    String email;
    String password;
    String name;
    String activationCode;
    Boolean deleted;
    Boolean activated;

    @Override
    protected Account self() {
        return this;
    }
}
