package com.loki.service.dto;

import com.loki.domain.enumeration.PanierStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.loki.domain.Panier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PanierDTO implements Serializable {

    private Long id;

    private PanierStatus status;

    private ZonedDateTime created;

    private String createdBy;

    private ZonedDateTime updated;

    private String updatedBy;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanierStatus getStatus() {
        return status;
    }

    public void setStatus(PanierStatus status) {
        this.status = status;
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

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PanierDTO)) {
            return false;
        }

        PanierDTO panierDTO = (PanierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, panierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PanierDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
