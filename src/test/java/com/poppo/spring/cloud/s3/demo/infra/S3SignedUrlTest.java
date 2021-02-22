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
import java.util.stream.IntStream;

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
        String key = "task01/01/2020-05-10 18.20.59.jpg";

        IntStream.rangeClosed(0, 1000).forEach(it -> s3SignedUrl.generateSignedUrl(key));
        URL signedUrl = s3SignedUrl.generateSignedUrl(key);

        System.out.println(signedUrl);
    }
}