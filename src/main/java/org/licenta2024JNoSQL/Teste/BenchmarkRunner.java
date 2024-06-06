package org.licenta2024JNoSQL.Teste;


import org.licenta2024JNoSQL.Teste.Benchmark.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FeedbackBenchmark.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(5)
                .forks(2)
                .result("FeedbackBenchmark.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
}
