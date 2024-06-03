package org.licenta2024JNoSQL.Entities.Produs;

import org.licenta2024JNoSQL.Entities.Produs.Enums.*;
import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Meta.AbstractEntity;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@Entity("Produs")
public class Produs extends AbstractEntity {

    @Id
    private String codProdus;  // Primary key

    @Column
    @NotBlank
    private String nume;

    @Column
    @Min(0)
    @Max(5)
    private Double rating;

    @Column
    @NotNull
    private Status status;

    @Column
    @Min(0)
    private Double pret;

    @Column("Tag-uri")
    private List<@NotBlank String> tagUri;

    @Override
    public String getId() {
        return codProdus;
    }
}
