package org.licenta2024JNoSQL.Entities.Client;

import jakarta.nosql.mapping.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity("Client")
public class Client {

    @Id
    private String id;

    @Column
    private String codClient;

    @Column
    private String nume;

    @Column
    private String prenume;

    @Column
    private Date dataNastere;

    @Column
    private String email;

    @Column
    private String numarTelefon;

    @Column
    private int puncteLoialitate;

    @Column
    private String statusMembru;

    @Column
    private Instant lastActive;

    @Column
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
