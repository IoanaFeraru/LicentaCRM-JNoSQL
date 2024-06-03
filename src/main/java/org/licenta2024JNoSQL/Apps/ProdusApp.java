package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Produs.Enums.Status;
import org.licenta2024JNoSQL.Entities.Produs.Produs;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ProdusApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            Produs produs = new Produs();
            produs.setCodProdus("P2");
            produs.setNume("Laptop");
            produs.setRating(4.5);
            produs.setStatus(Status.INSTOCK);
            produs.setPret(1500.0);
            produs.setTagUri(List.of("Electronics", "Computer"));

            // Set creation metadata
            produs.onCreate();

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Produs>> violations = validator.validate(produs);

            if (violations.isEmpty()) {
                DocumentTemplate template = container.select(DocumentTemplate.class).get();
                template.insert(produs);

                final Optional<Produs> queriedProdus = template.find(Produs.class, "P1");
                System.out.println("query : " + queriedProdus);

                // Update metadata
                queriedProdus.ifPresent(p -> {
                    p.onUpdate();
                    template.update(p);
                });

                //DocumentDeleteQuery deleteQuery = delete().from("Produs").where("_id").eq("P1").build();
                //template.delete(deleteQuery);

                System.out.println("query again: " +
                        template.find(Produs.class, "P1"));
            } else {
                for (ConstraintViolation<Produs> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
        System.exit(0);
    }
}
