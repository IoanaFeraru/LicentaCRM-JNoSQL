package org.licenta2024JNoSQL.Repositories;

import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.licenta2024JNoSQL.Entities.Produs.Produs;

import java.util.List;

public interface ProdusRepository extends Repository<Produs, String> {

    @Query("select * from Produs where codProdus = @codProdus")
    List<Produs> queryByCodProdus(@Param("codProdus") String codProdus);

    List<Produs> findByCodProdus(String codProdus);

    @Query("select * from Produs where nume = @nume")
    List<Produs> queryByNume(@Param("nume") String nume);

    List<Produs> findByNume(String nume);

    @Query("select * from Produs where rating = @rating")
    List<Produs> queryByRating(@Param("rating") Double rating);

    List<Produs> findByRating(Double rating);

    @Query("select * from Produs where status = @status")
    List<Produs> queryByStatus(@Param("status") String status);

    List<Produs> findByStatus(String status);

    @Query("select * from Produs where pret = @pret")
    List<Produs> queryByPret(@Param("pret") Double pret);

    List<Produs> findByPret(Double pret);

    @Query("select * from Produs where tagUri = @tagUri")
    List<Produs> queryByTagUri(@Param("tagUri") List<String> tagUri);

    List<Produs> findByTagUri(List<String> tagUri);
}