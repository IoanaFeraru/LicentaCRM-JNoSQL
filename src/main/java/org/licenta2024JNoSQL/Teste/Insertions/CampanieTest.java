package org.licenta2024JNoSQL.Teste.Insertions;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Campanie.Campanie;
import org.licenta2024JNoSQL.Entities.Campanie.Comunicare;
import org.licenta2024JNoSQL.Entities.Campanie.Segment;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Status;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Metoda;
import org.licenta2024JNoSQL.Entities.Campanie.Enums.Tip;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;
import org.licenta2024JNoSQL.Repositories.CampanieRepository;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.OfertaRepository;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;

public class CampanieTest {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            createCampanii(container);
        }
        System.exit(0);
    }

    public static void createCampanii(SeContainer container) {
        CampanieRepository campanieRepository = container.select(CampanieRepository.class).get();
        OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();
        DocumentTemplate template = container.select(DocumentTemplate.class).get();
        ClientRepository clientRepository = container.select(ClientRepository.class).get();
        List<Client> allClients = clientRepository.findAll();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        List<Oferta> allOffers = ofertaRepository.findAll();

        for (int i = 0; i < allOffers.size(); i++) {
            Oferta offer = allOffers.get(i);

            Campanie campanie = new Campanie();
            campanie.setCodCampanie(i + 1);
            campanie.setCodOferta(offer.getCodOferta());
            campanie.setNume("Campanie pentru Oferta " + offer.getCodOferta());
            campanie.setDataStart(new Date());
            campanie.setDataStop(null);
            campanie.setTip(Tip.EN_MASSE);

            Comunicare comunicare = new Comunicare();
            comunicare.setCodComunicare(i + 1);
            comunicare.setScop("Promovare oferta " + offer.getCodOferta());
            comunicare.setStatus(Status.SENT);
            comunicare.setMetoda(Metoda.EMAIL);

            Random random = new Random();
            int numberOfClients = random.nextInt(allClients.size()) + 1;
            List<String> randomClients = IntStream.range(0, numberOfClients)
                    .mapToObj(index -> allClients.get(random.nextInt(allClients.size())).getCodClient())
                    .distinct()
                    .collect(Collectors.toList());
            Segment segment = new Segment();
            segment.setCodSegment(i + 1);
            segment.setNume("Segment pentru Oferta " + offer.getCodOferta());
            segment.setDataCreare(new Date().toString());
            segment.setCriterii(List.of("criteriu1", "criteriu2"));
            segment.setClienti(randomClients);

            comunicare.setSegment(segment);
            campanie.setComunicare(comunicare);

            Set<ConstraintViolation<Campanie>> violations = validator.validate(campanie);

            if (violations.isEmpty()) {
                template.insert(campanie);
                campanieRepository.save(campanie);

                List<Campanie> queriedCampanieList = campanieRepository.findByCodCampanie(campanie.getCodCampanie());
                if (!queriedCampanieList.isEmpty()) {
                    System.out.println("query : " + queriedCampanieList.get(0));
                } else {
                    System.out.println("No Campanie found with codCampanie: " + campanie.getCodCampanie());
                }

                List<Campanie> queriedCampanieListAgain = campanieRepository.findByCodCampanie(campanie.getCodCampanie());
                System.out.println("query again: " + queriedCampanieListAgain);
            } else {
                for (ConstraintViolation<Campanie> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }
}
