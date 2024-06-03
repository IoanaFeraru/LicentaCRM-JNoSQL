package org.licenta2024JNoSQL.Apps;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Adresa;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Entities.Client.Embeded.IstoricPuncte;
import org.licenta2024JNoSQL.Entities.Client.Embeded.WishList;
import org.licenta2024JNoSQL.Entities.Client.Enums.Judet;
import org.licenta2024JNoSQL.Entities.Client.Enums.StatusMembru;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.licenta2024JNoSQL.Utilities.FeedbackUtil;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ClientApp {

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer
                .newInstance().initialize()) {

            DocumentTemplate template = container.select(DocumentTemplate.class).get();
            ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
            ClientRepository clientRepository = container.select(ClientRepository.class).get();
            FeedbackUtil feedbackUtil = new FeedbackUtil(clientRepository, produsRepository, template);

            Client client = new Client();
            client.setCodClient("C1");
            client.setNume("John");
            client.setPrenume("Doe");
            client.setDataNastere(new Date());
            client.setEmail("john.doe@example.com");
            client.setNumarTelefon("1234567890");
            client.setPuncteLoialitate(100);
            client.setStatusMembru(StatusMembru.GOLD);
            client.setLastActive(new Date());

            Adresa adresa1 = new Adresa();
            adresa1.setJudet(Judet.BUCURESTI);
            adresa1.setCodPostal(12345);
            adresa1.setStrada("Main St.");
            adresa1.setBloc("B1");

            WishList wishList1 = new WishList();
            wishList1.setCodProdus("P123");
            wishList1.setDataAdaugare(new Date());

            IstoricPuncte istoricPuncte1 = new IstoricPuncte();
            istoricPuncte1.setCodAchizitie(1);
            istoricPuncte1.setValoarePuncte(50);
            istoricPuncte1.setDataProcesare(new Date());

            Feedback feedback1 = new Feedback();
            feedback1.setCodProdus("P123");
            feedback1.setRating(5);

            client.setAdresa(List.of(adresa1));
            client.setWishList(List.of(wishList1));
            client.setIstoricPuncte(List.of(istoricPuncte1));
            client.setFeedback(List.of(feedback1));
            client.setTagUri(List.of("VIP", "Regular"));

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Client>> violations = validator.validate(client);

            if (violations.isEmpty()) {
                template.insert(client);

                // Call FeedbackUtil to update the product rating
                feedbackUtil.updateProductRating(feedback1.getCodProdus());

                final Optional<Client> queriedClient = template.find(Client.class, "C1");
                System.out.println("query : " + queriedClient);

                // Uncomment below lines to delete the entity if needed
                // DocumentDeleteQuery deleteQuery = delete().from("Client").where("_id").eq("C1").build();
                // template.delete(deleteQuery);

                System.out.println("query again: " +
                        template.find(Client.class, "C1"));
            } else {
                for (ConstraintViolation<Client> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
        System.exit(0);
    }
}
