package org.licenta2024JNoSQL.Teste.Benchmark;

import jakarta.nosql.mapping.document.DocumentTemplate;
import org.licenta2024JNoSQL.Entities.Produs.Enums.Status;
import org.licenta2024JNoSQL.Entities.Produs.Produs;
import org.openjdk.jmh.annotations.*;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class ProdusBenchmark {

    private DocumentTemplate template;
    private Validator validator;

    @State(Scope.Thread)
    public static class BenchmarkState {
        int currentIndex = 1;
    }

    @Setup(Level.Trial)
    public void setUp() {
        SeContainer container = SeContainerInitializer.newInstance().initialize();
        template = container.select(DocumentTemplate.class).get();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkAddProduct(BenchmarkState state) {
        Produs produs = new Produs();
        int i = state.currentIndex++;
        produs.setCodProdus("P" + i);
        produs.setNume("Produs" + i);
        produs.setStatus(Status.INSTOCK);
        produs.setPret(60.50);
        produs.setTagUri(List.of("Tag" + i, "Tag"+(i+1), "Tag"+(i+3), "CommonTag"));
        Set<ConstraintViolation<Produs>> violations = validator.validate(produs);

        if (violations.isEmpty()) {
            template.insert(produs);
            System.out.println("Inserted product: " + produs.getCodProdus() + " with price: " + produs.getPret());
        } else {
            for (ConstraintViolation<Produs> violation : violations) {
                System.out.println(violation.getMessage());
            }
        }
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void benchmarkFindById(BenchmarkState state) {
        String productId = "P" + (state.currentIndex);
        Optional<Produs> produs = template.find(Produs.class, productId);
        if (produs.isPresent()) {
            System.out.println("Found product: " + produs.get().getCodProdus() + " with price: " + produs.get().getPret());
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
    }
}
