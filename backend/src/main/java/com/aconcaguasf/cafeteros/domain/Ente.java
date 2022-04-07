package com.aconcaguasf.cafeteros.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Ente.
 */
@Entity
@Table(name = "ente")
public class Ente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name", length = 200, nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Ente id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ente name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Ente activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ente)) {
            return false;
        }
        return id != null && id.equals(((Ente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ente{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", activated='" + getActivated() + "'" +
            "}";
    }
}
