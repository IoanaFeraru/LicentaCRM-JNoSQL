package org.licenta2024JNoSQL.Entities.Achizitie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LinieAchizitie {

    @Column
    private String codProdus;

    @Column
    private int cantitate;
}
