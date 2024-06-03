package org.licenta2024JNoSQL.Entities.Campanie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Status;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Metoda;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Embeddable
public class Comunicare {

    @Column
    private int codComunicare;

    @Column
    private String scop;

    @Column
    @NotNull
    private Status status;

    @Column
    @NotNull
    private Metoda metoda;

    @Column
    @NotNull
    private Segment segment;
}
