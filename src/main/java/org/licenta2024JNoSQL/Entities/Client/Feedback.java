package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Feedback {
    @Column
    private String codProdus;

    @Column
    private int rating;
}