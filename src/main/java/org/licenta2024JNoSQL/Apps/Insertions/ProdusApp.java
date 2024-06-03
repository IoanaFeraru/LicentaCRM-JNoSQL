package org.licenta2024JNoSQL.Apps.Insertions;

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
import java.util.Random;
import java.util.Set;

public class ProdusApp {

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            addMultipleProducts(container, 10);
        }
        System.exit(0);
    }

    public static void addMultipleProducts(SeContainer container, int numberOfProducts) {
        DocumentTemplate template = container.select(DocumentTemplate.class).get();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Random random = new Random();

        for (int i = 1; i <= numberOfProducts; i++) {
            Produs produs = new Produs();
            produs.setCodProdus("P" + i);
            produs.setNume("Produs" + i);
            produs.setStatus(Status.INSTOCK);

            double randomPrice = 10 + (8000 - 10) * random.nextDouble();
            double formattedPrice = Math.round(randomPrice * 100.0) / 100.0;
            produs.setPret(formattedPrice);

            produs.setTagUri(List.of("Tag" + i, "CommonTag"));

            Set<ConstraintViolation<Produs>> violations = validator.validate(produs);

            if (violations.isEmpty()) {
                template.insert(produs);
                System.out.println("Inserted product: " + produs.getCodProdus() + " with price: " + produs.getPret());
            } else {
                for (ConstraintViolation<Produs> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }
}
