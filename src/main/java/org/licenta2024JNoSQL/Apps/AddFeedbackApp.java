package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.licenta2024JNoSQL.Utils.FeedbackUtil;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class AddFeedbackApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            DocumentTemplate template = container.select(DocumentTemplate.class).get();
            ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
            ClientRepository clientRepository = container.select(ClientRepository.class).get();
            FeedbackUtil feedbackUtil = new FeedbackUtil(clientRepository, produsRepository, template);

            String clientId = "C3"; // The ID of the existing client
            String productId = "P1"; // The product ID for the feedback
            int rating = 5; // The new rating

            // Fetch the existing client
            List<Client> clientList = clientRepository.findByCodClient(clientId);

            if (!clientList.isEmpty()) {
                Client client = clientList.get(0);

                // Create new feedback
                Feedback newFeedback = new Feedback();
                newFeedback.setCodProdus(productId);
                newFeedback.setRating(rating);

                // Add the new feedback to the client
                client.getFeedback().add(newFeedback);

                // Validate the updated client entity
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<Client>> violations = validator.validate(client);

                if (violations.isEmpty()) {
                    // Update the client in the database
                    template.update(client);

                    // Update the product rating
                    feedbackUtil.updateProductRating(productId);

                    System.out.println("Feedback added and product rating updated successfully.");
                } else {
                    for (ConstraintViolation<Client> violation : violations) {
                        System.out.println(violation.getMessage());
                    }
                }
            } else {
                System.out.println("Client not found with ID: " + clientId);
            }
        }
        System.exit(0);
    }
}
