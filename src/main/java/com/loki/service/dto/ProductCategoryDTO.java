package com.loki.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.loki.domain.ProductCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCategoryDTO implements Serializable {

    private Long id;

    private String name;

    private ZonedDateTime created;

    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategoryDTO)) {
            return false;
        }

        ProductCategoryDTO productCategoryDTO = (ProductCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
