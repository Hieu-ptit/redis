package com.viettel.core.model.entity;

import com.dslplatform.json.CompiledJson;
import com.viettel.core.converter.RewardMetadataConverter;
import com.viettel.core.model.BoxInvitationMetadata;
import com.viettel.core.model.RewardBox;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@CompiledJson
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Audited
public class Employee extends BaseEntity<Employee> {

    @Id
    @SequenceGenerator(name = "employee_id_seq", sequenceName = "employee_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_id_seq")
    Long id;
    String name;
    @Type(type = "jsonb")
    List<RewardBox> boxes;
    @Convert(converter = RewardMetadataConverter.class)
    BoxInvitationMetadata metadata;
    @Type(type = "jsonb")
    Company company;


    @Override
    protected Employee self() {
        return this;
    }
}
