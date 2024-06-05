package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Adresa;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Entities.Client.Embeded.IstoricPuncte;
import org.licenta2024JNoSQL.Entities.Client.Embeded.WishList;
import org.licenta2024JNoSQL.Entities.Client.Enums.StatusMembru;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity("Client")
public class Client {

    @Id
    private String codClient;

    @Column
    @NotBlank
    private String nume;

    @Column
    @NotBlank
    private String prenume;

    @Column
    @NotNull
    private Date dataNastere;

    @Column
    @Email
    @NotBlank
    private String email;

    @Column
    @NotBlank
    private String numarTelefon;

    @Column
    @Min(0)
    private int puncteLoialitate;

    @Column
    @NotNull
    private StatusMembru statusMembru;

    @Column
    @NotNull
    private Date lastActive;

    @Column
    @Size(min = 1)
    private List<Adresa> adresa;

    @Column
    private List<WishList> wishList;

    @Column
    private List<IstoricPuncte> istoricPuncte;

    @Column
    private List<Feedback> feedback;

    @Column("Tag-uri")
    private List<String> tagUri;
}
