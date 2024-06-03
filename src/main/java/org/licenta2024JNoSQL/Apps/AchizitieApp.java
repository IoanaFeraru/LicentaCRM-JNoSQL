package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Achizitie.Achizitie;
import org.licenta2024JNoSQL.Entities.Achizitie.LinieAchizitie;
import org.licenta2024JNoSQL.Repositories.AchizitieRepository;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.OfertaRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.licenta2024JNoSQL.Utilities.AchizitieUtil;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class AchizitieApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            DocumentTemplate template = container.select(DocumentTemplate.class).get();
            ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
            OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();
            AchizitieRepository achizitieRepository = container.select(AchizitieRepository.class).get();
            ClientRepository clientRepository = container.select(ClientRepository.class).get();

            Achizitie achizitie = new Achizitie();
            achizitie.setCodAchizitie(1);
            achizitie.setData(new Date());
            achizitie.setCodClient("C3");

            LinieAchizitie linie1 = new LinieAchizitie();
            linie1.setCodProdus("P3"); // Assuming P3 is the product in the offer
            linie1.setCantitate(1);

            LinieAchizitie linie2 = new LinieAchizitie();
            linie2.setCodProdus("P1");
            linie2.setCantitate(1);

            achizitie.setLinieAchizitie(List.of(linie1, linie2));
            achizitie.setCodOferta("O2"); // Assuming there is an offer with code O2

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Achizitie>> violations = validator.validate(achizitie);

            if (violations.isEmpty()) {
                AchizitieUtil.calculateValoareAchizitie(achizitie, produsRepository, ofertaRepository);
                AchizitieUtil.calculateValoarePuncte(achizitie, ofertaRepository);

                template.insert(achizitie);
                achizitieRepository.save(achizitie);

                AchizitieUtil.updatePuncteClient(achizitie, clientRepository, template);

                System.out.println("Achizitie saved with total value and points calculated.");
            } else {
                for (ConstraintViolation<Achizitie> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
        System.exit(0);
    }
}
