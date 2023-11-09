package com.loki.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.loki.domain.LineOfCommand} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LineOfCommandDTO implements Serializable {

    private Long id;

    private Integer quantity;

    private BigDecimal total;

    private ZonedDateTime created;

    private String createdBy;

    private ZonedDateTime updated;

    private String updatedBy;

    private ProductDTO product;

    private ClientDTO client;

    private PanierDTO panier;

    private CommandDTO command;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public PanierDTO getPanier() {
        return panier;
    }

    public void setPanier(PanierDTO panier) {
        this.panier = panier;
    }

    public CommandDTO getCommand() {
        return command;
    }

    public void setCommand(CommandDTO command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineOfCommandDTO)) {
            return false;
        }

        LineOfCommandDTO lineOfCommandDTO = (LineOfCommandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lineOfCommandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LineOfCommandDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", total=" + getTotal() +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", product=" + getProduct() +
            ", client=" + getClient() +
            ", panier=" + getPanier() +
            ", command=" + getCommand() +
            "}";
    }
}
