package org.licenta2024JNoSQL.Repositories;

import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.licenta2024JNoSQL.Entities.Achizitie.Achizitie;

import java.util.List;
import java.util.Optional;

public interface AchizitieRepository extends Repository<Achizitie, Integer> {

    @Query("select * from Achizitie where codClient = @codClient")
    List<Achizitie> findByCodClient(@Param("codClient") String codClient);

    @Query("select * from Achizitie where codAchizitie = @codAchizitie")
    Optional<Achizitie> findByCodAchizitie(@Param("codAchizitie") int codAchizitie);
}
