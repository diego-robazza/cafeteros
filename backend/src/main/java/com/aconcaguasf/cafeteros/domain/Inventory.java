package com.aconcaguasf.cafeteros.domain;

import com.aconcaguasf.cafeteros.domain.enumeration.AuditStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name_audit", length = 200, nullable = false, unique = true)
    private String nameAudit;

    @Enumerated(EnumType.STRING)
    @Column(name = "audit_status")
    private AuditStatus auditStatus;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @ManyToOne(optional = false)
    @NotNull
    private Ente ente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Inventory id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNameAudit() {
        return this.nameAudit;
    }

    public Inventory nameAudit(String nameAudit) {
        this.setNameAudit(nameAudit);
        return this;
    }

    public void setNameAudit(String nameAudit) {
        this.nameAudit = nameAudit;
    }

    public AuditStatus getAuditStatus() {
        return this.auditStatus;
    }

    public Inventory auditStatus(AuditStatus auditStatus) {
        this.setAuditStatus(auditStatus);
        return this;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Inventory startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public Ente getEnte() {
        return this.ente;
    }

    public void setEnte(Ente ente) {
        this.ente = ente;
    }

    public Inventory ente(Ente ente) {
        this.setEnte(ente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return id != null && id.equals(((Inventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", nameAudit='" + getNameAudit() + "'" +
            ", auditStatus='" + getAuditStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            "}";
    }
}
