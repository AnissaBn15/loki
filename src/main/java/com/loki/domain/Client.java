package com.loki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loki.domain.enumeration.ClientType;
import com.loki.domain.enumeration.Role;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ClientType type;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Size(min = 1, max = 255)
    @Column(name = "first_name", length = 255)
    private String firstName;

    @Size(min = 1, max = 255)
    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated")
    private ZonedDateTime updated;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "linesCommands", "client" }, allowSetters = true)
    private Set<Panier> paniers = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnoreProperties(value = { "linesCommands", "paiement", "client", "fournisseur" }, allowSetters = true)
    private Set<Command> commands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Client code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ClientType getType() {
        return this.type;
    }

    public Client type(ClientType type) {
        this.setType(type);
        return this;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Client active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Client firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Client lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBankAccountNumber() {
        return this.bankAccountNumber;
    }

    public Client bankAccountNumber(String bankAccountNumber) {
        this.setBankAccountNumber(bankAccountNumber);
        return this;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public Client role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return this.address;
    }

    public Client address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return this.country;
    }

    public Client country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Client created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Client createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Client updated(ZonedDateTime updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Client updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<Panier> getPaniers() {
        return this.paniers;
    }

    public void setPaniers(Set<Panier> paniers) {
        if (this.paniers != null) {
            this.paniers.forEach(i -> i.setClient(null));
        }
        if (paniers != null) {
            paniers.forEach(i -> i.setClient(this));
        }
        this.paniers = paniers;
    }

    public Client paniers(Set<Panier> paniers) {
        this.setPaniers(paniers);
        return this;
    }

    public Client addPaniers(Panier panier) {
        this.paniers.add(panier);
        panier.setClient(this);
        return this;
    }

    public Client removePaniers(Panier panier) {
        this.paniers.remove(panier);
        panier.setClient(null);
        return this;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setClient(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setClient(this));
        }
        this.commands = commands;
    }

    public Client commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Client addCommands(Command command) {
        this.commands.add(command);
        command.setClient(this);
        return this;
    }

    public Client removeCommands(Command command) {
        this.commands.remove(command);
        command.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", type='" + getType() + "'" +
            ", active='" + getActive() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", bankAccountNumber='" + getBankAccountNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            ", address='" + getAddress() + "'" +
            ", country='" + getCountry() + "'" +
            ", created='" + getCreated() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
