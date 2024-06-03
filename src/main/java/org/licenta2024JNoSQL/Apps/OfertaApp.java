package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.Status;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.TipReducere;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.licenta2024JNoSQL.Repositories.OfertaRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class OfertaApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();
            ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
            DocumentTemplate template = container.select(DocumentTemplate.class).get();

            // Create a new Oferta instance
            Oferta oferta = new Oferta();
            oferta.setCodOferta("O2");
            oferta.setStatus(Status.ACTIVE);
            oferta.setTipReducere(TipReducere.PRODUS);
            oferta.setValoareReducere(10.0);
            oferta.setCostPuncte(100);

            // Fetch all products from Produs collection
            List<Produs> produse = produsRepository.findAll();
            oferta.setProduse(produse);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Oferta>> violations = validator.validate(oferta);

            if (violations.isEmpty()) {
                template.insert(oferta);
                ofertaRepository.save(oferta);

                List<Oferta> queriedOfertaList = ofertaRepository.findByCodOferta(oferta.getCodOferta());
                if (queriedOfertaList.size() == 1) {
                    System.out.println("query : " + queriedOfertaList.get(0));
                } else {
                    System.out.println("Expected one result, but found: " + queriedOfertaList.size());
                }

                // Uncomment below lines to delete the entity if needed
                //DocumentDeleteQuery deleteQuery = delete().from("Oferta").where("_id").eq(oferta.getId()).build();
                //template.delete(deleteQuery);

                List<Oferta> queriedOfertaListAgain = ofertaRepository.findByCodOferta(oferta.getCodOferta());
                System.out.println("query again: " + queriedOfertaListAgain);
            } else {
                for (ConstraintViolation<Oferta> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
        System.exit(0);
    }
}
