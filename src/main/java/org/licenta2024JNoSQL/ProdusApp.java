package org.licenta2024JNoSQL;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Produs;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.util.List;
import java.util.Optional;

public class ProdusApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            Produs produs = new Produs();
            produs.setId("1");
            produs.setCodProdus("ABC123");
            produs.setNume("Laptop");
            produs.setRating(4.5);
            produs.setStatus("Available");
            produs.setPret(1500.0);
            produs.setTagUri(List.of("Electronics", "Computer"));

            DocumentTemplate template = container.select(DocumentTemplate.class)
                    .get();
            template.insert(produs);

            final Optional<Produs> queriedProdus = template.find(Produs.class, "1");
            System.out.println("query : " + queriedProdus);

            //DocumentDeleteQuery deleteQuery = delete().from("Produs").where("_id").eq("1").build();
            //template.delete(deleteQuery);

            System.out.println("query again: " +
                    template.find(Produs.class, "1"));
        }
        System.exit(0);
    }
}