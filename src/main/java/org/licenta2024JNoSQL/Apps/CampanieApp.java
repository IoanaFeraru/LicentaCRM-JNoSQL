package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Campanie.Campanie;
import org.licenta2024JNoSQL.Entities.Campanie.Comunicare;
import org.licenta2024JNoSQL.Entities.Campanie.Segment;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Status;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Metoda;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Tip;
import org.licenta2024JNoSQL.Repositories.CampanieRepository;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class CampanieApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            CampanieRepository campanieRepository = container.select(CampanieRepository.class).get();
            DocumentTemplate template = container.select(DocumentTemplate.class).get();

            // Create a new Campanie instance
            Campanie campanie = new Campanie();
            campanie.setCodCampanie(1);
            campanie.setCodOferta("O1");
            campanie.setNume("Campanie Noua");
            campanie.setDataStart(new Date());
            campanie.setDataStop(new Date(System.currentTimeMillis() + 86400000L)); // 1 day later
            campanie.setTip(Tip.EN_MASSE);

            // Create a new Comunicare instance
            Comunicare comunicare = new Comunicare();
            comunicare.setCodComunicare(1);
            comunicare.setScop("Promovare noua oferta");
            comunicare.setStatus(Status.PENDING);
            comunicare.setMetoda(Metoda.EMAIL);

            // Create a new Segment instance
            Segment segment = new Segment();
            segment.setCodSegment(1);
            segment.setNume("Segment Nou");
            segment.setDataCreare(new Date().toString());
            segment.setCriterii(List.of("criteriu1", "criteriu2"));
            segment.setClienti(List.of("C1", "C2"));

            // Set segment to comunicare
            comunicare.setSegment(segment);

            // Set comunicare to campanie
            campanie.setComunicare(comunicare);

            // Validate Campanie entity
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Campanie>> violations = validator.validate(campanie);

            if (violations.isEmpty()) {
                // Insert Campanie entity into the database
                template.insert(campanie);
                campanieRepository.save(campanie);

                // Query and print the inserted Campanie
                List<Campanie> queriedCampanieList = campanieRepository.findByCodCampanie(campanie.getCodCampanie());
                if (!queriedCampanieList.isEmpty()) {
                    System.out.println("query : " + queriedCampanieList.get(0));
                } else {
                    System.out.println("No Campanie found with codCampanie: " + campanie.getCodCampanie());
                }

                // DocumentDeleteQuery deleteQuery = delete().from("Campanie").where("_id").eq(campanie.getCodCampanie()).build();
                // template.delete(deleteQuery);

                List<Campanie> queriedCampanieListAgain = campanieRepository.findByCodCampanie(campanie.getCodCampanie());
                System.out.println("query again: " + queriedCampanieListAgain);
            } else {
                for (ConstraintViolation<Campanie> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
        System.exit(0);
    }
}
