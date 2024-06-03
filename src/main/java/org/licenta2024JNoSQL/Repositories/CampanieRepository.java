package org.licenta2024JNoSQL.Repositories;

import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.licenta2024JNoSQL.Entities.Campanie.Campanie;

import java.util.List;

public interface CampanieRepository extends Repository<Campanie, Integer> {

    @Query("select * from Campanie where codCampanie = @codCampanie")
    List<Campanie> findByCodCampanie(@Param("codCampanie") int codCampanie);

    @Query("select * from Campanie where codOferta = @codOferta")
    List<Campanie> findByCodOferta(@Param("codOferta") String codOferta);

    @Query("select * from Campanie where tip = @tip")
    List<Campanie> findByTip(@Param("tip") String tip);
}
