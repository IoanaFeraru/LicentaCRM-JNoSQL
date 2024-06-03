package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Adresa {
    @Column
    private String judet;
    @Column
    private int codPostal;
    @Column
    private String strada;
    @Column
    private String bloc;
}