package org.licenta2024JNoSQL.Entities.Achizitie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity("Achizitie")
public class Achizitie {

    @Id
    private String id;

    @Column
    private int codAchizitie;

    @Column
    private Date data;

    @Column
    private String codClient;

    @Column
    private List<LinieAchizitie> linieAchizitie;

    @Column
    private String codOferta;

    @Column
    private String valoareAchizitie;

    @Column
    private String valoarePuncte;
}
