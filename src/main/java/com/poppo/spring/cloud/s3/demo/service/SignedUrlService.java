package com.poppo.spring.cloud.s3.demo.service;

import com.poppo.spring.cloud.s3.demo.infra.S3Loader;
import com.poppo.spring.cloud.s3.demo.infra.S3SignedUrl;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class SignedUrlService {
    private final S3Loader s3Loader;
    private final S3SignedUrl s3SignedUrl;

    public SignedUrlService(final S3Loader s3Loader, final S3SignedUrl s3SignedUrl) {
        this.s3Loader = s3Loader;
        this.s3SignedUrl = s3SignedUrl;
    }

    public List<String> getSignedUrls(final String folderPath) {
        List<String> keys = s3Loader.pickStaticFolder(folderPath);

        return keys.stream()
                .map(s3SignedUrl::generateSignedUrl)
                .map(URL::toString)
                .collect(Collectors.toList());
    }

    public List<CompletableFuture<String>> getSignedUrls2(final String folderPath) throws ExecutionException, InterruptedException {
        List<String> keys = s3Loader.pickStaticFolder(folderPath);

        List<CompletableFuture<String>> futures = keys.stream()
                .map(this::createCompletableFuture)
                .collect(Collectors.toList());

        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        all.get();

        return futures;
    }

    private CompletableFuture<String> createCompletableFuture(String objectKey) {
        return CompletableFuture.supplyAsync(() -> s3SignedUrl.generateSignedUrl(objectKey).toString());
    }
}
