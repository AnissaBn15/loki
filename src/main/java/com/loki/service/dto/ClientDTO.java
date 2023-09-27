package com.loki.service.dto;

import com.loki.domain.enumeration.ClientType;
import com.loki.domain.enumeration.Role;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.loki.domain.Client} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private ClientType type;

    @NotNull
    private Boolean active;

    @Size(min = 1, max = 255)
    private String firstName;

    @Size(min = 1, max = 255)
    private String lastName;

    private String bankAccountNumber;

    private String email;

    private Role role;

    private String address;

    @NotNull
    private String country;

    private ZonedDateTime created;

    private String createdBy;

    private ZonedDateTime updated;

    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
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
