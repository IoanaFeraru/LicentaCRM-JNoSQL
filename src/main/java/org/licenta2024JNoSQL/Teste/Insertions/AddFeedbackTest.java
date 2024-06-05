package org.licenta2024JNoSQL.Teste.Insertions;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Achizitie.Achizitie;
import org.licenta2024JNoSQL.Entities.Achizitie.LinieAchizitie;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Repositories.AchizitieRepository;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.licenta2024JNoSQL.Utilities.FeedbackUtil;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;

public class AddFeedbackTest {

    private static final Random random = new Random();

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            createFeedbacks(container);
        }
        System.exit(0);
    }

    public static void createFeedbacks(SeContainer container) {
        DocumentTemplate template = container.select(DocumentTemplate.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
        ClientRepository clientRepository = container.select(ClientRepository.class).get();
        AchizitieRepository achizitieRepository = container.select(AchizitieRepository.class).get();
        FeedbackUtil feedbackUtil = new FeedbackUtil(clientRepository, produsRepository, template);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        List<Client> allClients = clientRepository.findAll();

        for (Client client : allClients) {
            List<Achizitie> clientAcquisitions = achizitieRepository.findByCodClient(client.getCodClient());

            List<String> acquiredProducts = clientAcquisitions.stream()
                    .flatMap(achizitie -> achizitie.getLinieAchizitie().stream())
                    .map(LinieAchizitie::getCodProdus)
                    .distinct()
                    .collect(Collectors.toList());

            for (String productId : acquiredProducts) {
                if (random.nextBoolean()) {
                    int rating = random.nextInt(5) + 1;

                    Feedback newFeedback = new Feedback();
                    newFeedback.setCodProdus(productId);
                    newFeedback.setRating(rating);

                    if (client.getFeedback() == null) {
                        client.setFeedback(new ArrayList<>());
                    }
                    client.getFeedback().add(newFeedback);
                }
            }

            Set<ConstraintViolation<Client>> violations = validator.validate(client);

            if (violations.isEmpty()) {
                template.update(client);

                for (String productId : acquiredProducts) {
                    feedbackUtil.updateProductRating(productId);
                }

                System.out.println("Feedback added and product ratings updated for client: " + client.getCodClient());
            } else {
                for (ConstraintViolation<Client> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }
}
