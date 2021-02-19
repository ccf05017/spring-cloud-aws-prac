package com.poppo.spring.cloud.s3.demo.learningtest;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.poppo.spring.cloud.s3.demo.infra.S3Loader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CompletableFutureLearningTest {
    @Autowired
    private S3Loader s3Loader;

    @Autowired
    private ExecutorService fixedThreadPool;

    @Test
    void getFileInfo() throws InterruptedException {
        List<Callable<S3ObjectSummary>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(createTask());
        }

        List<Future<S3ObjectSummary>> results = fixedThreadPool.invokeAll(tasks);

        results.forEach(result -> {
            try {
                System.out.println(result.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void getFileInfoByCompletableFuture() throws ExecutionException, InterruptedException {
        List<CompletableFuture<S3ObjectSummary>> tasks = IntStream.rangeClosed(0, 10)
                .mapToObj(it -> this.createCompletableFutureTask())
                .collect(Collectors.toList());
        CompletableFuture<Void> futures = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()]));
        futures.get();

        tasks.forEach(task -> assertThat(task.isDone()).isTrue());
        tasks.forEach(task -> {
            try {
                System.out.println(task.get().getKey());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private Callable<S3ObjectSummary> createTask() {
        return () -> s3Loader.pickStaticNumber("task01/01", 1).get(0);
    }

    private CompletableFuture<S3ObjectSummary> createCompletableFutureTask() {
        return CompletableFuture.supplyAsync(() -> s3Loader.pickStaticNumber("task01/01", 1).get(0));
    }
}
