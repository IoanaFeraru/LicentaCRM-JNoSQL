package org.licenta2024JNoSQL.Entities.Campanie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Tip;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity("Campanie")
public class Campanie {

    @Id
    private int codCampanie;

    @Column
    private String codOferta;

    @Column
    private String nume;

    @Column
    @NotNull
    private Date dataStart;

    @Column
    private Date dataStop;

    @Column
    @NotNull
    private Tip tip;

    @Column
    @NotNull
    private Comunicare comunicare;
}
