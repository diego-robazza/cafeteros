package com.aconcaguasf.cafeteros.service.dto;

import com.aconcaguasf.cafeteros.domain.enumeration.AuditStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aconcaguasf.cafeteros.domain.Inventory} entity.
 */
public class InventoryDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 1, max = 200)
    private String nameAudit;

    private AuditStatus auditStatus;

    private ZonedDateTime startDate;

    private EnteDTO ente;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNameAudit() {
        return nameAudit;
    }

    public void setNameAudit(String nameAudit) {
        this.nameAudit = nameAudit;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public EnteDTO getEnte() {
        return ente;
    }

    public void setEnte(EnteDTO ente) {
        this.ente = ente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryDTO)) {
            return false;
        }

        InventoryDTO inventoryDTO = (InventoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryDTO{" +
            "id='" + getId() + "'" +
            ", nameAudit='" + getNameAudit() + "'" +
            ", auditStatus='" + getAuditStatus() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", ente=" + getEnte() +
            "}";
    }
}
