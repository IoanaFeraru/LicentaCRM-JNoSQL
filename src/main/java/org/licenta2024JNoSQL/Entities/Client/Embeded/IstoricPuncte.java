package org.licenta2024JNoSQL.Entities.Client.Embeded;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class IstoricPuncte {
    @Column
    @NotNull
    private int codAchizitie;

    @Column
    @NotNull
    private int valoarePuncte;

    @Column
    @NotNull
    private Date dataProcesare;
}
