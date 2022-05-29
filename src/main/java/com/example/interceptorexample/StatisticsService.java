package com.example.interceptorexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.context.annotation.Primary;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.DoubleStream;

@Configuration
public class StatisticsService {

    public static final String STAT_MEAN = "mean";
    public static final String STAT_STANDARD_DEVIATION = "std";
    public static final String STAT_VARIANCE = "var";
    private final ConcurrentHashMap<String, Integer> urlServeCounters = new ConcurrentHashMap<>();

    private DescriptiveStatistics stats = null;
    private final AtomicBoolean statsInvalidated = new AtomicBoolean(true);

    @Bean
    @Primary
    public static StatisticsService statisticsServiceSingleton() {
        return new StatisticsService();
    }

    public void countUp(String url) { // Relies on the concurrency of urlServeCounters to avoid synchronization
        urlServeCounters.put(url, urlServeCounters.getOrDefault(url, 0) + 1);
        statsInvalidated.set(true);
        System.err.println("Counting up " + url);
    }

    public int getCountFor(String url) { // Relies on the concurrency of urlServeCounters to avoid synchronization
        return urlServeCounters.get(url);
    }

    private synchronized DescriptiveStatistics getValidStats() { // must be synchronized since its own logic is unsafe
        if (statsInvalidated.get()) {
            Collection<Integer> intValues = urlServeCounters.values();
            DoubleStream doubleStream = intValues.stream().flatMapToDouble(integer -> DoubleStream.of(Double.valueOf(integer)));
            stats = new DescriptiveStatistics(doubleStream.toArray());
            statsInvalidated.set(true);
        }
        return stats;
    }

    public double mean() {
        return getValidStats().getMean();
    }

    public double std() {
        return getValidStats().getStandardDeviation();
    }

    public double variance() {
        return getValidStats().getVariance();
    }

    public HashMap<String, Double> getStats() {
        HashMap<String, Double> results = new HashMap<>();
        DescriptiveStatistics validStats = getValidStats(); // getting the valid stats object just once to avoid extra synchronization
        results.put(STAT_MEAN, validStats.getMean());
        results.put(STAT_STANDARD_DEVIATION, validStats.getStandardDeviation());
        results.put(STAT_VARIANCE, validStats.getVariance());
        return results;
    }
}
