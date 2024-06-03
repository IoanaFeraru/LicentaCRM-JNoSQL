package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Embeddable
public class WishList {
    @Column
    private String codProdus;

    @Column
    private Date dataAdaugare;
}