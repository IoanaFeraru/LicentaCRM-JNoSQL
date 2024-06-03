package org.licenta2024JNoSQL.Entities.Client.Embeded;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Client.Enums.Judet;

import javax.validation.constraints.*;

@Getter
@Setter
@Embeddable
public class Adresa {
    @Column
    @NotNull
    private Judet judet;

    @Column
    @Min(999)
    @Max(10000)
    private int codPostal;

    @Column
    @NotBlank
    private String strada;

    @Column
    private String bloc;
}
