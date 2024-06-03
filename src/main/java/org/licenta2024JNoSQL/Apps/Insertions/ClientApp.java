package org.licenta2024JNoSQL.Apps.Insertions;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Adresa;
import org.licenta2024JNoSQL.Entities.Client.Embeded.WishList;
import org.licenta2024JNoSQL.Entities.Client.Enums.Judet;
import org.licenta2024JNoSQL.Entities.Client.Enums.StatusMembru;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;

public class ClientApp {

    private static final Random random = new Random();

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            addMultipleClients(container, 5);
        }
        System.exit(0);
    }

    public static void addMultipleClients(SeContainer container, int numberOfClients) {
        DocumentTemplate template = container.select(DocumentTemplate.class).get();
        ClientRepository clientRepository = container.select(ClientRepository.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();

        List<Produs> allProducts = produsRepository.findAll();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        StatusMembru[] statusMembruValues = StatusMembru.values();

        for (int i = 1; i <= numberOfClients; i++) {
            Client client = new Client();
            client.setCodClient("C" + i);
            client.setNume("Nume");
            client.setPrenume("Prenume");
            client.setDataNastere(new Date());
            client.setEmail("nume.prenume" + i + "@example.com");
            client.setNumarTelefon("0700" + i);
            client.setPuncteLoialitate(random.nextInt(10001));
            client.setStatusMembru(statusMembruValues[random.nextInt(statusMembruValues.length)]);
            client.setLastActive(new Date());

            Adresa adresa = new Adresa();
            adresa.setJudet(Judet.BUCURESTI);
            adresa.setCodPostal(12345);
            adresa.setStrada("Strada 1");
            adresa.setBloc("B" + i);

            int numberOfWishListItems = random.nextInt(allProducts.size()) + 1;
            List<WishList> wishList = IntStream.range(0, numberOfWishListItems)
                    .mapToObj(index -> {
                        Produs selectedProduct = allProducts.get(random.nextInt(allProducts.size()));
                        WishList wl = new WishList();
                        wl.setCodProdus(selectedProduct.getCodProdus());
                        wl.setDataAdaugare(new Date());
                        return wl;
                    })
                    .distinct()
                    .collect(Collectors.toList());

            client.setAdresa(List.of(adresa));
            client.setWishList(wishList);
            client.setIstoricPuncte(null);
            client.setFeedback(null);
            client.setTagUri(null);

            Set<ConstraintViolation<Client>> violations = validator.validate(client);

            if (violations.isEmpty()) {
                template.insert(client);
                clientRepository.save(client);

                final Optional<Client> queriedClient = template.find(Client.class, "C" + i);
                System.out.println("Inserted and queried: " + queriedClient);

                System.out.println("query again: " +
                        template.find(Client.class, "C" + i));
            } else {
                for (ConstraintViolation<Client> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }
}
