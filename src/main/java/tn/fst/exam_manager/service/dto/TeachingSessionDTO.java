package tn.fst.exam_manager.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link tn.fst.exam_manager.domain.TeachingSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeachingSessionDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Instant startHour;

    @NotNull
    private Instant endHour;

    @NotNull
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartHour() {
        return startHour;
    }

    public void setStartHour(Instant startHour) {
        this.startHour = startHour;
    }

    public Instant getEndHour() {
        return endHour;
    }

    public void setEndHour(Instant endHour) {
        this.endHour = endHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachingSessionDTO)) {
            return false;
        }

        TeachingSessionDTO teachingSessionDTO = (TeachingSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teachingSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeachingSessionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startHour='" + getStartHour() + "'" +
            ", endHour='" + getEndHour() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
