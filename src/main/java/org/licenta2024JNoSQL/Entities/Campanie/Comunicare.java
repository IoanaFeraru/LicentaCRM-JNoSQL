package org.licenta2024JNoSQL.Entities.Campanie;


import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Comunicare {

    @Column
    private int codComunicare;

    @Column
    private String scop;

    @Column
    private String status;

    @Column
    private String metoda;

    @Column
    private Segment segment;
}
