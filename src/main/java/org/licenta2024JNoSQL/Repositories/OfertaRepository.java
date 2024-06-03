package org.licenta2024JNoSQL.Repositories;

import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.Repository;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;

import java.util.List;

public interface OfertaRepository extends Repository<Oferta, String> {

    @Query("select * from Oferta where codOferta = @codOferta")
    List<Oferta> findByCodOferta(@Param("codOferta") String codOferta);

    @Query("select * from Oferta")
    List<Oferta> findAll();
}
