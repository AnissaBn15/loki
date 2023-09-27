package com.loki.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.loki.domain.enumeration.Role;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fournisseur implements Serializable {

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
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "company")
    private String company;

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

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "linesCommands", "paiement", "client", "fournisseur" }, allowSetters = true)
    private Set<Command> commands = new HashSet<>();

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "command", "fournisseur" }, allowSetters = true)
    private Set<Paiement> paiements = new HashSet<>();

    @OneToMany(mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "fournisseur", "productCategory", "images" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fournisseur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Fournisseur code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Fournisseur active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCompany() {
        return this.company;
    }

    public Fournisseur company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Fournisseur firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Fournisseur lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBankAccountNumber() {
        return this.bankAccountNumber;
    }

    public Fournisseur bankAccountNumber(String bankAccountNumber) {
        this.setBankAccountNumber(bankAccountNumber);
        return this;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Fournisseur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return this.role;
    }

    public Fournisseur role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return this.address;
    }

    public Fournisseur address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return this.country;
    }

    public Fournisseur country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Fournisseur created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Fournisseur createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Fournisseur updated(ZonedDateTime updated) {
        this.setUpdated(updated);
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Fournisseur updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Set<Command> getCommands() {
        return this.commands;
    }

    public void setCommands(Set<Command> commands) {
        if (this.commands != null) {
            this.commands.forEach(i -> i.setFournisseur(null));
        }
        if (commands != null) {
            commands.forEach(i -> i.setFournisseur(this));
        }
        this.commands = commands;
    }

    public Fournisseur commands(Set<Command> commands) {
        this.setCommands(commands);
        return this;
    }

    public Fournisseur addCommands(Command command) {
        this.commands.add(command);
        command.setFournisseur(this);
        return this;
    }

    public Fournisseur removeCommands(Command command) {
        this.commands.remove(command);
        command.setFournisseur(null);
        return this;
    }

    public Set<Paiement> getPaiements() {
        return this.paiements;
    }

    public void setPaiements(Set<Paiement> paiements) {
        if (this.paiements != null) {
            this.paiements.forEach(i -> i.setFournisseur(null));
        }
        if (paiements != null) {
            paiements.forEach(i -> i.setFournisseur(this));
        }
        this.paiements = paiements;
    }

    public Fournisseur paiements(Set<Paiement> paiements) {
        this.setPaiements(paiements);
        return this;
    }

    public Fournisseur addPaiements(Paiement paiement) {
        this.paiements.add(paiement);
        paiement.setFournisseur(this);
        return this;
    }

    public Fournisseur removePaiements(Paiement paiement) {
        this.paiements.remove(paiement);
        paiement.setFournisseur(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setFournisseur(null));
        }
        if (products != null) {
            products.forEach(i -> i.setFournisseur(this));
        }
        this.products = products;
    }

    public Fournisseur products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Fournisseur addProducts(Product product) {
        this.products.add(product);
        product.setFournisseur(this);
        return this;
    }

    public Fournisseur removeProducts(Product product) {
        this.products.remove(product);
        product.setFournisseur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", active='" + getActive() + "'" +
            ", company='" + getCompany() + "'" +
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
