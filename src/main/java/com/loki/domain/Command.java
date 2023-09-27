package com.loki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loki.domain.enumeration.CommandStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "command_number", unique = true)
    private String commandNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommandStatus status;

    @Column(name = "total", precision = 21, scale = 2)
    private BigDecimal total;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "command")
    @JsonIgnoreProperties(value = { "product", "panier", "command" }, allowSetters = true)
    private Set<LineOfCommand> linesCommands = new HashSet<>();

    @JsonIgnoreProperties(value = { "command", "fournisseur" }, allowSetters = true)
    @OneToOne(mappedBy = "command")
    private Paiement paiement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paniers", "commands" }, allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commands", "paiements", "products" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Command id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandNumber() {
        return this.commandNumber;
    }

    public Command commandNumber(String commandNumber) {
        this.setCommandNumber(commandNumber);
        return this;
    }

    public void setCommandNumber(String commandNumber) {
        this.commandNumber = commandNumber;
    }

    public CommandStatus getStatus() {
        return this.status;
    }

    public Command status(CommandStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Command total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Command created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Command createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Command updated(ZonedDateTime updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Command updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<LineOfCommand> getLinesCommands() {
        return this.linesCommands;
    }

    public void setLinesCommands(Set<LineOfCommand> lineOfCommands) {
        if (this.linesCommands != null) {
            this.linesCommands.forEach(i -> i.setCommand(null));
        }
        if (lineOfCommands != null) {
            lineOfCommands.forEach(i -> i.setCommand(this));
        }
        this.linesCommands = lineOfCommands;
    }

    public Command linesCommands(Set<LineOfCommand> lineOfCommands) {
        this.setLinesCommands(lineOfCommands);
        return this;
    }

    public Command addLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.add(lineOfCommand);
        lineOfCommand.setCommand(this);
        return this;
    }

    public Command removeLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.remove(lineOfCommand);
        lineOfCommand.setCommand(null);
        return this;
    }

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        if (this.paiement != null) {
            this.paiement.setCommand(null);
        }
        if (paiement != null) {
            paiement.setCommand(this);
        }
        this.paiement = paiement;
    }

    public Command paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Command client(Client client) {
        this.setClient(client);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Command fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Command)) {
            return false;
        }
        return id != null && id.equals(((Command) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Command{" +
            "id=" + getId() +
            ", commandNumber='" + getCommandNumber() + "'" +
            ", status='" + getStatus() + "'" +
            ", total=" + getTotal() +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
