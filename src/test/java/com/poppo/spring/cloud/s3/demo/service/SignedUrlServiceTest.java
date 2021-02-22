package com.poppo.spring.cloud.s3.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

// 통합테스트
// AWS S3 의존성 있음
@SpringBootTest
class SignedUrlServiceTest {
    @Autowired
    private SignedUrlService signedUrlService;

    @DisplayName("해당 경로 파일들에 대한 Signed URL을 생성할 수 있다. (블로킹)")
    @Test
    void createSignedUrls() {
        String folderPath = "task01/01";
        int expectedSize = 1000;

        List<String> signedUrls = signedUrlService.getSignedUrls(folderPath);

        assertThat(signedUrls).hasSize(expectedSize);
    }

    @Test
    void simpleTest() throws ExecutionException, InterruptedException {
        String folderPath = "task01/01";

        List<CompletableFuture<String>> signedUrls2 = signedUrlService.getSignedUrls2(folderPath);

        signedUrls2.forEach(it -> assertThat(it.isDone()).isTrue());
    }
}