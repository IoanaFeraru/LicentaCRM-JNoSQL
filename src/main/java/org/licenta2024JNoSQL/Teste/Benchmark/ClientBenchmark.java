package org.licenta2024JNoSQL.Teste.Benchmark;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Client.Client;
import org.licenta2024JNoSQL.Entities.Client.Embeded.Adresa;
import org.licenta2024JNoSQL.Entities.Client.Embeded.WishList;
import org.licenta2024JNoSQL.Entities.Client.Enums.Judet;
import org.licenta2024JNoSQL.Entities.Client.Enums.StatusMembru;
import org.licenta2024JNoSQL.Repositories.ClientRepository;
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
public class ClientBenchmark {

    private DocumentTemplate template;
    private ClientRepository clientRepository;
    private Validator validator;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 1;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        clientRepository = container.select(ClientRepository.class).get();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkAddClient(BenchmarkState state) {
        Client client = new Client();
        int i = state.currentIndex++;
        client.setCodClient("C" + i);
        client.setNume("Nume");
        client.setPrenume("Prenume");
        client.setDataNastere(new Date());
        client.setEmail("nume.prenume" + i + "@example.com");
        client.setNumarTelefon("0700" + i);
        client.setPuncteLoialitate(1000);
        client.setStatusMembru(StatusMembru.GOLD);
        client.setLastActive(new Date());

        Adresa adresa = new Adresa();
        adresa.setJudet(Judet.BUCURESTI);
        adresa.setCodPostal(12345);
        adresa.setStrada("Strada 1");
        adresa.setBloc("B" + i);

        List<WishList> wishList = IntStream.range(1, 51)
                .mapToObj(index -> {
                    WishList wl = new WishList();
                    wl.setCodProdus("P" + index);
                    wl.setDataAdaugare(new Date());
                    return wl;
                })
                .collect(Collectors.toList());

        client.setAdresa(List.of(adresa));
        client.setWishList(wishList);
        client.setTagUri(List.of("Tag1", "Tag2", "Tag3"));

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        if (violations.isEmpty()) {
            template.insert(client);
            clientRepository.save(client);
        } else {
            violations.forEach(violation -> System.out.println(violation.getMessage()));
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkFindById(BenchmarkState state) {
        String clientId = "C" + state.currentIndex;
        Optional<Client> queriedClient = template.find(Client.class, clientId);
        queriedClient.ifPresentOrElse(
                client -> System.out.println("Found client: " + client.getCodClient()),
                () -> System.out.println("Client with ID " + clientId + " not found.")
        );
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
