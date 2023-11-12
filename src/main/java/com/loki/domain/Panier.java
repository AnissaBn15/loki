package com.loki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loki.domain.enumeration.PanierStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Panier.
 */
@Entity
@Table(name = "panier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PanierStatus status;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "panier")
    @JsonIgnoreProperties(value = { "product", "client", "panier", "command" }, allowSetters = true)
    private Set<LineOfCommand> linesCommands = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "paniers", "commands", "linesCommands" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Panier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PanierStatus getStatus() {
        return this.status;
    }

    public Panier status(PanierStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PanierStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Panier created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Panier createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Panier updated(ZonedDateTime updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Panier updatedBy(String updatedBy) {
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
            this.linesCommands.forEach(i -> i.setPanier(null));
        }
        if (lineOfCommands != null) {
            lineOfCommands.forEach(i -> i.setPanier(this));
        }
        this.linesCommands = lineOfCommands;
    }

    public Panier linesCommands(Set<LineOfCommand> lineOfCommands) {
        this.setLinesCommands(lineOfCommands);
        return this;
    }

    public Panier addLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.add(lineOfCommand);
        lineOfCommand.setPanier(this);
        return this;
    }

    public Panier removeLinesCommand(LineOfCommand lineOfCommand) {
        this.linesCommands.remove(lineOfCommand);
        lineOfCommand.setPanier(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Panier client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Panier)) {
            return false;
        }
        return id != null && id.equals(((Panier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Panier{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
