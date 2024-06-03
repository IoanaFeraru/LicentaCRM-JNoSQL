package org.licenta2024JNoSQL.Entities.Oferta;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.*;
import org.licenta2024JNoSQL.Entities.Produs.Produs;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@Entity("Oferta")
public class Oferta {

    @Id
    private String codOferta;

    @Column
    @NotNull
    private Status status;

    @Column
    @NotNull
    private TipReducere tipReducere;

    @Column
    @NotNull
    @Min(0)
    private Double valoareReducere;

    @Column
    private Integer costPuncte;

    @Column
    @Size(min = 1)
    private List<Produs> produse;
}
