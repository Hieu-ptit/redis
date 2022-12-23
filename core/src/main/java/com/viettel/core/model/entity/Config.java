package com.viettel.core.model.entity;

import com.dslplatform.json.CompiledJson;
import com.viettel.core.converter.ConfigDataConverter;
import com.viettel.core.model.ConfigValue;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@CompiledJson
@EqualsAndHashCode(callSuper = true)
@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Accessors(chain = true)
public class Config extends BaseEntity<Config> {
    @Id
    @SequenceGenerator(name = "config_id_seq", sequenceName = "config_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_id_seq")
    Long id;
    String key;
    @Convert(converter = ConfigDataConverter.class)
    ConfigValue value;

    @Override
    protected Config self() {
        return this;
    }
}
