package com.loki.service.dto;

import com.loki.domain.enumeration.CommandStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.loki.domain.Command} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommandDTO implements Serializable {

    private Long id;

    private String commandNumber;

    private CommandStatus status;

    private BigDecimal total;

    private ZonedDateTime created;

    private String createdBy;

    private ZonedDateTime updated;

    private String updatedBy;

    private ClientDTO client;

    private FournisseurDTO fournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandNumber() {
        return commandNumber;
    }

    public void setCommandNumber(String commandNumber) {
        this.commandNumber = commandNumber;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandDTO)) {
            return false;
        }

        CommandDTO commandDTO = (CommandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandDTO{" +
            "id=" + getId() +
            ", commandNumber='" + getCommandNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", total=" + getTotal() +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", client=" + getClient() +
            ", fournisseur=" + getFournisseur() +
            "}";
    }
}
