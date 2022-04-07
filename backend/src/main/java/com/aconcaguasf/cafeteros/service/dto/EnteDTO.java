package com.aconcaguasf.cafeteros.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.aconcaguasf.cafeteros.domain.Ente} entity.
 */
public class EnteDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @NotNull
    private Boolean activated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnteDTO)) {
            return false;
        }

        EnteDTO enteDTO = (EnteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnteDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", activated='" + getActivated() + "'" +
            "}";
    }
}
