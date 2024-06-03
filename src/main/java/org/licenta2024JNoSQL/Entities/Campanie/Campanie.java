package org.licenta2024JNoSQL.Entities.Campanie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity("Campanie")
public class Campanie {

    @Id
    private String id;

    @Column
    private int codCampanie;

    @Column
    private String codOferta;

    @Column
    private String nume;

    @Column
    private Date dataStart;

    @Column
    private Date dataStop;

    @Column
    private String tip;

    @Column
    private Comunicare comunicare;
}
