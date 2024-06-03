package org.licenta2024JNoSQL.Meta;

import jakarta.nosql.mapping.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractEntity {

    @Column("created_by_user")
    private String createdByUser;

    @Column("updated_by_user")
    private String updatedByUser;

    @Column("date_created")
    private LocalDateTime dateCreated;

    @Column("date_updated")
    private LocalDateTime dateUpdated;

    public abstract String getId();

    public void onCreate() {
        dateCreated = dateUpdated = LocalDateTime.now();
    }

    public void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }
}