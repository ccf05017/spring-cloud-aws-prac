package com.poppo.spring.cloud.s3.demo.infra;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Aws S3 의존성 있음
@SpringBootTest
class S3SignedUrlTest {
    @Autowired
    private S3SignedUrl s3SignedUrl;

    @Autowired
    private S3Loader s3Loader;

    private WebClient webClient;

    @Test
    void createNewSignedUrl() throws URISyntaxException {
        // Signed URL 생성
        String testFolder = "task01/01";
        List<S3ObjectSummary> summaries = s3Loader.pickStaticNumber(testFolder, 2000);
        S3ObjectSummary target = summaries.get(0);

        URL signedUrl = s3SignedUrl.generateSignedUrl(target.getKey());
        System.out.println("################################");
        System.out.println(signedUrl.toURI());

        // Signed URL 응답 확인
        webClient = WebClient.create();

        HttpStatus status = webClient.get()
                .uri(signedUrl.toURI())
                .exchangeToMono(res -> Mono.just(res.statusCode()))
                .block();

        assertThat(status).isEqualTo(HttpStatus.OK);
    }
}