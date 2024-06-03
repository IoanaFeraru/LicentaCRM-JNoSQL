package org.licenta2024JNoSQL.Entities.Client.Embeded;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class WishList {
    @Column
    @NotBlank
    private String codProdus;

    @Column
    @NotNull
    private Date dataAdaugare;
}
