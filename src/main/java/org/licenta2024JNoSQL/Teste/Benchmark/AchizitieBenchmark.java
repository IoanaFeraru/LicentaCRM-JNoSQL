package org.licenta2024JNoSQL.Teste.Benchmark;

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
import org.openjdk.jmh.annotations.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class AchizitieBenchmark {

    private DocumentTemplate template;
    private ProdusRepository produsRepository;
    private OfertaRepository ofertaRepository;
    private AchizitieRepository achizitieRepository;
    private ClientRepository clientRepository;
    private Validator validator;
    private List<Client> allClients;
    private List<Oferta> allOffers;
    private List<Produs> allProducts;
    private int currentCodAchizitie;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 1;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        produsRepository = container.select(ProdusRepository.class).get();
        ofertaRepository = container.select(OfertaRepository.class).get();
        achizitieRepository = container.select(AchizitieRepository.class).get();
        clientRepository = container.select(ClientRepository.class).get();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        allClients = clientRepository.findAll();
        allOffers = ofertaRepository.findAll();
        allProducts = produsRepository.findAll();

        Optional<Integer> maxCodAchizitieOpt = achizitieRepository.findAll()
                .stream()
                .map(Achizitie::getCodAchizitie)
                .max(Integer::compare);
        currentCodAchizitie = maxCodAchizitieOpt.orElse(1);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkCreateAchizitie(BenchmarkState state) {
        int clientIndex = state.currentIndex % allClients.size();
        Client client = allClients.get(clientIndex);
        state.currentIndex++;

        Achizitie achizitie = new Achizitie();
        currentCodAchizitie++;
        achizitie.setCodAchizitie(currentCodAchizitie);
        achizitie.setData(new Date());
        achizitie.setCodClient(client.getCodClient());

        List<LinieAchizitie> liniiAchizitie = IntStream.range(0, 10)
                .mapToObj(index -> {
                    Produs selectedProduct = allProducts.get(index % allProducts.size());
                    LinieAchizitie linie = new LinieAchizitie();
                    linie.setCodProdus(selectedProduct.getCodProdus());
                    linie.setCantitate(2);
                    return linie;
                })
                .collect(Collectors.toList());

        achizitie.setLinieAchizitie(liniiAchizitie);

        if (!allOffers.isEmpty() && clientIndex % 2 == 0) {
            Oferta selectedOffer = allOffers.get(clientIndex % allOffers.size());
            achizitie.setCodOferta(selectedOffer.getCodOferta());
        }

        Set<ConstraintViolation<Achizitie>> violations = validator.validate(achizitie);

        if (violations.isEmpty()) {
            AchizitieUtil.calculateValoareAchizitie(achizitie, produsRepository, ofertaRepository);
            AchizitieUtil.calculateValoarePuncte(achizitie, ofertaRepository);

            template.insert(achizitie);
            achizitieRepository.save(achizitie);

            AchizitieUtil.updatePuncteClient(achizitie, clientRepository, template);
        } else {
            violations.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkFindById(BenchmarkState state) {
        int codAchizitie = state.currentIndex;
        List<Achizitie> queriedAchizitieList = achizitieRepository.findByCodAchizitie(codAchizitie);
        if (queriedAchizitieList.size() == 1) {
            System.out.println("Found achizitie: " + queriedAchizitieList.get(0).getCodAchizitie());
        } else {
            System.out.println("Achizitie with ID " + codAchizitie + " not found or found multiple.");
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
