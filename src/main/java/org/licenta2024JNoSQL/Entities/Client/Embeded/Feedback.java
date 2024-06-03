package org.licenta2024JNoSQL.Entities.Client.Embeded;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@Embeddable
public class Feedback {
    @Column
    @NotBlank
    private String codProdus;

    @Column
    @Min(0)
    @Max(5)
    private int rating;
}
