package org.licenta2024JNoSQL.Entities.Campanie;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Embeddable
public class Segment {

    @Column
    private int codSegment;

    @Column
    @NotBlank
    private String nume;

    @Column
    @NotNull
    private String dataCreare;

    @Column
    private List<String> criterii;

    @Column
    private List<String> clienti;
}
