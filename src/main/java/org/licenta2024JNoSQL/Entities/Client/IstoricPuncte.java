package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Embeddable
public class IstoricPuncte {
    @Column
    private int codAchizitie;

    @Column
    private int valoarePuncte;

    @Column
    private Date dataProcesare;
}