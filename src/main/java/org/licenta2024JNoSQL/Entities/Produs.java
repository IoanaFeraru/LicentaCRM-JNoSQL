package org.licenta2024JNoSQL.Entities;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity("Produs")
public class Produs {

    @Id
    private String id;
    @Column
    private String codProdus;
    @Column
    private String nume;
    @Column
    private Double rating;
    @Column
    private String status;
    @Column
    private Double pret;
    @Column
    private List<String> tagUri;
}
