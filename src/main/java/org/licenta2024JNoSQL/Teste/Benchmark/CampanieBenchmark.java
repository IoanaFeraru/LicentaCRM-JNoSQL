package org.licenta2024JNoSQL.Teste.Benchmark;

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
import org.openjdk.jmh.annotations.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class CampanieBenchmark {

    private DocumentTemplate template;
    private CampanieRepository campanieRepository;
    private Validator validator;
    private List<Client> allClients;
    private List<Oferta> allOffers;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 0;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        campanieRepository = container.select(CampanieRepository.class).get();
        ClientRepository clientRepository = container.select(ClientRepository.class).get();
        OfertaRepository ofertaRepository = container.select(OfertaRepository.class).get();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        allClients = clientRepository.findAll();
        allOffers = ofertaRepository.findAll();

        if (allClients.size() < 200) {
            throw new RuntimeException("Not enough clients for the benchmark. Need at least 200 clients.");
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkCreateCampanie(BenchmarkState state) {
        int i = state.currentIndex % allOffers.size();
        state.currentIndex++;
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

        List<String> randomClients = IntStream.range(0, 200)
                .mapToObj(index -> allClients.get(index).getCodClient())
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
        } else {
            violations.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkFindById(BenchmarkState state) {
        int codCampanie = (state.currentIndex % allOffers.size()) + 1;
        List<Campanie> queriedCampanieList = campanieRepository.findByCodCampanie(codCampanie);
        if (queriedCampanieList.size() == 1) {
            System.out.println("Found campaign: " + queriedCampanieList.get(0).getCodCampanie());
        } else {
            System.out.println("Campaign with ID " + codCampanie + " not found or found multiple.");
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
