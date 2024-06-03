package org.licenta2024JNoSQL.Entities.Achizitie;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Embeddable
public class LinieAchizitie {

    @Column
    @NotBlank
    private String codProdus;

    @Column
    @Min(1)
    @NotNull
    private int cantitate;

    @Column
    private double price; // Price per line (product price*quantity)
}
