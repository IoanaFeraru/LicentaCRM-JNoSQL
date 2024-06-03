package org.licenta2024JNoSQL.Repositories;

import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.licenta2024JNoSQL.Entities.Client.*;

import java.util.List;

public interface ClientRepository extends Repository<Client, String> {

    @Query("select * from Client where codClient = @codClient")
    List<Client> queryByCodClient(@Param("codClient") String codClient);

    List<Client> findByCodClient(String codClient);

    @Query("select * from Client where email = @email")
    List<Client> queryByEmail(@Param("email") String email);

    List<Client> findByEmail(String email);

    @Query("select * from Client where numarTelefon = @numarTelefon")
    List<Client> queryByNumarTelefon(@Param("numarTelefon") String numarTelefon);

    List<Client> findByNumarTelefon(String numarTelefon);

    @Query("select * from Client where statusMembru = @statusMembru")
    List<Client> queryByStatusMembru(@Param("statusMembru") String statusMembru);

    List<Client> findByStatusMembru(String statusMembru);

    @Query("select * from Client where adresa.judet = @judet")
    List<Client> queryByAdresaJudet(@Param("judet") String judet);

    List<Client> findByAdresaJudet(String judet);

    @Query("select * from Client where wishList.codProdus = @codProdus")
    List<Client> queryByWishListCodProdus(@Param("codProdus") String codProdus);

    List<Client> findByWishListCodProdus(String codProdus);

    @Query("select * from Client where istoricPuncte.codAchizitie = @codAchizitie")
    List<Client> queryByIstoricPuncteCodAchizitie(@Param("codAchizitie") int codAchizitie);

    List<Client> findByIstoricPuncteCodAchizitie(int codAchizitie);

    @Query("select * from Client where feedback.codProdus = @codProdus")
    List<Client> queryByFeedbackCodProdus(@Param("codProdus") String codProdus);

    List<Client> findByFeedbackCodProdus(String codProdus);
}
