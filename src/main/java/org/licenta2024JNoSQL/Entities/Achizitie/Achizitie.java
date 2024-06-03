package org.licenta2024JNoSQL.Entities.Achizitie;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity("Achizitie")
public class Achizitie {

    @Id
    private int codAchizitie;

    @Column
    @NotNull
    private Date data;

    @Column
    @NotBlank
    private String codClient;

    @Column
    @Size(min = 1)
    private List<@NotNull LinieAchizitie> linieAchizitie;

    @Column
    private String codOferta;

    @Column
    private String valoareAchizitie;

    @Column
    private String valoarePuncte;
}
