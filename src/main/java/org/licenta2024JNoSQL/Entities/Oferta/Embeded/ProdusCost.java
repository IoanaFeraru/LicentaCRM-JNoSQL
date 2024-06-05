package org.licenta2024JNoSQL.Entities.Oferta.Embeded;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Produs.Produs;

@Getter
@Setter
@Embeddable
public class ProdusCost {
    @Column
    private Produs produs;
    @Column
    private Integer costPuncte;
    @Column
    private String codFinal;
}
