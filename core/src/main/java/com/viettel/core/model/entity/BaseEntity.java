package com.viettel.core.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@MappedSuperclass
@Data
@Accessors(chain = true)
public abstract class BaseEntity<T> {
  @JsonProperty("created_at")
  OffsetDateTime createdAt;
  @JsonProperty("updated_at")
  OffsetDateTime updatedAt;

  protected abstract T self();

  public T setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return self();
  }

  public T setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return self();
  }
}
