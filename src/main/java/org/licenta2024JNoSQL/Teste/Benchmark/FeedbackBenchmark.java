package org.licenta2024JNoSQL.Teste.Benchmark;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Achizitie.Achizitie;
import org.licenta2024JNoSQL.Entities.Achizitie.LinieAchizitie;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Feedback;
import org.licenta2024JNoSQL.Repositories.AchizitieRepository;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
import org.licenta2024JNoSQL.Repositories.ProdusRepository;
import org.licenta2024JNoSQL.Utilities.FeedbackUtil;
import org.openjdk.jmh.annotations.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
public class FeedbackBenchmark {

    private DocumentTemplate template;
    private ClientRepository clientRepository;
    private AchizitieRepository achizitieRepository;
    private FeedbackUtil feedbackUtil;
    private Validator validator;
    private List<Client> allClients;
    private Random random;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 1;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        ProdusRepository produsRepository = container.select(ProdusRepository.class).get();
        clientRepository = container.select(ClientRepository.class).get();
        achizitieRepository = container.select(AchizitieRepository.class).get();
        feedbackUtil = new FeedbackUtil(clientRepository, produsRepository, template);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        allClients = clientRepository.findAll();
        random = new Random();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkAddFeedback(BenchmarkState state) {
        Client client = allClients.get(state.currentIndex % allClients.size());
        state.currentIndex++;

        List<Achizitie> clientAcquisitions = achizitieRepository.findByCodClient(client.getCodClient());

        List<String> acquiredProducts = clientAcquisitions.stream()
                .flatMap(achizitie -> achizitie.getLinieAchizitie().stream())
                .map(LinieAchizitie::getCodProdus)
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i < acquiredProducts.size(); i++) {
            if (random.nextBoolean()) {
                String productId = acquiredProducts.get(i);
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
        } else {
            violations.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
