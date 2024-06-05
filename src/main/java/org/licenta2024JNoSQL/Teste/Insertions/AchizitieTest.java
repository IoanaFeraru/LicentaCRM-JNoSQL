package org.licenta2024JNoSQL.Teste.Insertions;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Achizitie.Achizitie;
import org.licenta2024JNoSQL.Entities.Achizitie.LinieAchizitie;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
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
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.ConstraintViolation;

public class AchizitieTest {

    private static final Random random = new Random();

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            createAchizitii(container);
        }
        System.exit(0);
    }

    public static void createAchizitii(SeContainer container) {
        DocumentTemplate template = container.select(DocumentTemplate.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
        OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();
        AchizitieRepository achizitieRepository = container.select(AchizitieRepository.class).get();
        ClientRepository clientRepository = container.select(ClientRepository.class).get();

        List<Client> allClients = clientRepository.findAll();
        List<Oferta> allOffers = ofertaRepository.findAll();
        List<Produs> allProducts = produsRepository.findAll();

        Optional<Integer> maxCodAchizitieOpt = achizitieRepository.findAll()
                .stream()
                .map(Achizitie::getCodAchizitie)
                .max(Integer::compare);
        int currentCodAchizitie = maxCodAchizitieOpt.orElse(1);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (Client client : allClients) {
            int numberOfAchizitii = random.nextInt(10) + 1;

            for (int j = 0; j < numberOfAchizitii; j++) {
                Achizitie achizitie = new Achizitie();
                currentCodAchizitie++;
                achizitie.setCodAchizitie(currentCodAchizitie);
                achizitie.setData(new Date());
                achizitie.setCodClient(client.getCodClient());

                int numberOfProducts = random.nextInt(allProducts.size()) + 1;
                List<LinieAchizitie> liniiAchizitie = IntStream.range(0, numberOfProducts)
                        .mapToObj(index -> {
                            Produs selectedProduct = allProducts.get(random.nextInt(allProducts.size()));
                            LinieAchizitie linie = new LinieAchizitie();
                            linie.setCodProdus(selectedProduct.getCodProdus());
                            linie.setCantitate(random.nextInt(10) + 1);
                            return linie;
                        })
                        .collect(Collectors.toList());

                achizitie.setLinieAchizitie(liniiAchizitie);

                if (!allOffers.isEmpty() && random.nextBoolean()) {
                    Oferta selectedOffer = allOffers.get(random.nextInt(allOffers.size()));
                    achizitie.setCodOferta(selectedOffer.getCodOferta());
                }

                Set<ConstraintViolation<Achizitie>> violations = validator.validate(achizitie);

                if (violations.isEmpty()) {
                    AchizitieUtil.calculateValoareAchizitie(achizitie, produsRepository, ofertaRepository);
                    AchizitieUtil.calculateValoarePuncte(achizitie, ofertaRepository);

                    template.insert(achizitie);
                    achizitieRepository.save(achizitie);

                    AchizitieUtil.updatePuncteClient(achizitie, clientRepository, template);

                    System.out.println("Achizitie saved with total value and points calculated for client: " + client.getCodClient());
                } else {
                    for (ConstraintViolation<Achizitie> violation : violations) {
                        System.out.println(violation.getMessage());
                    }
                }
            }
        }
    }
}
