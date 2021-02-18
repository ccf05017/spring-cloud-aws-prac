package com.poppo.spring.cloud.s3.demo.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

// Aws S3 의존성 있음
@SpringBootTest
class S3SignedUrlTest {
    @Autowired
    private S3SignedUrl s3SignedUrl;

    private WebClient webClient;

    @Test
    void createNewSignedUrl() throws URISyntaxException {
        // Signed URL 생성
        String testFolder = "task01";
        String testFile = "360FBD2E-D88F-4A11-B78A-62EC87CE28F5.jpeg";

        URL signedUrl = s3SignedUrl.generateSignedUrl(testFolder + "/" + testFile);

        assertThat(signedUrl.getPath()).contains(testFile);

        // Signed URL 응답 확인
        webClient = WebClient.create();

        HttpStatus status = webClient.get()
                .uri(signedUrl.toURI())
                .exchangeToMono(res -> Mono.just(res.statusCode()))
                .block();

        assertThat(status).isEqualTo(HttpStatus.OK);
    }
}