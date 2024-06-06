package org.licenta2024JNoSQL.Teste.Benchmark;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Oferta.Embeded.ProdusCost;
import org.licenta2024JNoSQL.Entities.Oferta.Oferta;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.Status;
import org.licenta2024JNoSQL.Entities.Oferta.Enums.TipReducere;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.licenta2024JNoSQL.Repositories.OfertaRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.openjdk.jmh.annotations.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class OfertaBenchmark {

    private DocumentTemplate template;
    private OfertaRepository ofertaRepository;
    private Validator validator;
    private List<ProdusCost> productsCost;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 1;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        ofertaRepository = container.select(OfertaRepository.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        productsCost = IntStream.range(0, 50)
                .mapToObj(index -> {
                    Produs selectedProduct = produsRepository.findByCodProdus("P" + (index + 1));
                    ProdusCost pc = new ProdusCost();
                    pc.setProdus(selectedProduct);
                    pc.setCostPuncte(120);
                    return pc;
                })
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkAddOffers(BenchmarkState state) {
        int i = state.currentIndex++;
        addProductOffer(i);
        addVoucherOffer(i);
        addPercentOffer(i);
    }

    private void addProductOffer(int i) {
        Oferta ofertaP = new Oferta();
        ofertaP.setCodOferta("O" + i);
        ofertaP.setStatus(Status.ACTIVE);
        ofertaP.setTipReducere(TipReducere.PRODUS);
        ofertaP.setProduseCost(productsCost);
        ofertaP.setValoareReducere(1.0);

        Set<ConstraintViolation<Oferta>> violationsP = validator.validate(ofertaP);

        if (violationsP.isEmpty()) {
            template.insert(ofertaP);
            ofertaRepository.save(ofertaP);
        } else {
            violationsP.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    private void addVoucherOffer(int i) {
        Oferta ofertaV = new Oferta();
        ofertaV.setCodOferta("V" + i);
        ofertaV.setStatus(Status.ACTIVE);
        ofertaV.setTipReducere(TipReducere.VOUCHER);
        ofertaV.setValoareReducere(100.00);
        ofertaV.setProduseCost(null);

        Set<ConstraintViolation<Oferta>> violationsV = validator.validate(ofertaV);

        if (violationsV.isEmpty()) {
            template.insert(ofertaV);
            ofertaRepository.save(ofertaV);
        } else {
            violationsV.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    private void addPercentOffer(int i) {
        Oferta ofertaPr = new Oferta();
        ofertaPr.setCodOferta("P" + i);
        ofertaPr.setStatus(Status.ACTIVE);
        ofertaPr.setTipReducere(TipReducere.PROCENT);
        ofertaPr.setValoareReducere(0.4);
        ofertaPr.setProduseCost(null);

        Set<ConstraintViolation<Oferta>> violationsPr = validator.validate(ofertaPr);

        if (violationsPr.isEmpty()) {
            template.insert(ofertaPr);
            ofertaRepository.save(ofertaPr);
        } else {
            violationsPr.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }


    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkFindById(BenchmarkState state) {
        String ofertaId = "O" + state.currentIndex;
        List<Oferta> queriedOfertaList = ofertaRepository.findByCodOferta(ofertaId);
        if (queriedOfertaList.size() == 1) {
            System.out.println("Found offer: " + queriedOfertaList.get(0).getCodOferta());
        } else {
            System.out.println("Offer with ID " + ofertaId + " not found or found multiple.");
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
