package org.licenta2024JNoSQL.Apps.Insertions;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Oferta.Embeded.ProdusCost;
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
import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OfertaApp {

    private static final Random random = new Random();

    public static void main(String[] args) {
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            addMultipleOffers(container, 5); // Insert 5 offers
        }
        System.exit(0);
    }

    public static void addMultipleOffers(SeContainer container, int numberOfOffers) {
        OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
        DocumentTemplate template = container.select(DocumentTemplate.class).get();

        List<Produs> allProducts = produsRepository.findAll();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Oferta ofertaP = new Oferta();
        ofertaP.setCodOferta("O1");
        ofertaP.setStatus(Status.ACTIVE);
        ofertaP.setTipReducere(TipReducere.PRODUS);
        int numberOfProducts = random.nextInt(allProducts.size()) + 1;
        List<ProdusCost> randomProducts = IntStream.range(0, numberOfProducts)
                .mapToObj(index -> {
                    Produs selectedProduct = allProducts.get(random.nextInt(allProducts.size()));
                    ProdusCost pc = new ProdusCost();
                    pc.setProdus(selectedProduct);
                    pc.setCostPuncte((int) (2 * selectedProduct.getPret()));
                    return pc;
                })
                .distinct()
                .collect(Collectors.toList());
        ofertaP.setProduseCost(randomProducts);
        ofertaP.setValoareReducere(1.0);

        Set<ConstraintViolation<Oferta>> violationsP = validator.validate(ofertaP);

        if (violationsP.isEmpty()) {
            template.insert(ofertaP);
            ofertaRepository.save(ofertaP);

            List<Oferta> queriedOfertaList = ofertaRepository.findByCodOferta(ofertaP.getCodOferta());
            if (queriedOfertaList.size() == 1) {
                System.out.println("Inserted and queried: " + queriedOfertaList.get(0));
            } else {
                System.out.println("Expected one result, but found: " + queriedOfertaList.size());
            }
        } else {
            for (ConstraintViolation<Oferta> violation : violationsP) {
                System.out.println(violation.getMessage());
            }
        }

        // Insert the other offers
        for (int i = 2; i <= numberOfOffers; i++) {
            Oferta oferta = new Oferta();
            oferta.setCodOferta("O" + i);
            oferta.setStatus(Status.ACTIVE);

            TipReducere tipReducere = TipReducere.values()[random.nextInt(TipReducere.values().length - 1)];
            oferta.setTipReducere(tipReducere);

            switch (tipReducere) {
                case VOUCHER:
                    oferta.setValoareReducere(20.0 + (random.nextInt(10) * 20.0));
                    oferta.setProduseCost(null);
                    break;
                case PROCENT:
                    oferta.setValoareReducere(0.1 + (random.nextInt(10) * 0.1));
                    oferta.setProduseCost(null);
                    break;
            }

            Set<ConstraintViolation<Oferta>> violations = validator.validate(oferta);

            if (violations.isEmpty()) {
                template.insert(oferta);
                ofertaRepository.save(oferta);

                List<Oferta> queriedOfertaList = ofertaRepository.findByCodOferta(oferta.getCodOferta());
                if (queriedOfertaList.size() == 1) {
                    System.out.println("Inserted and queried: " + queriedOfertaList.get(0));
                } else {
                    System.out.println("Expected one result, but found: " + queriedOfertaList.size());
                }
            } else {
                for (ConstraintViolation<Oferta> violation : violations) {
                    System.out.println(violation.getMessage());
                }
            }
        }
    }
}
