package org.licenta2024JNoSQL.Entities;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity("Oferta")
public class Oferta {

    @Id
    private String id;

    @Column
    private String codOferta;

    @Column
    private String status;

    @Column
    private String tipReducere;

    @Column
    private Double valoareReducere;

    @Column
    private Integer costPuncte;

    @Column
    private List<Produs> produse;
}
