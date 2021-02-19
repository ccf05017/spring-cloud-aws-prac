package com.poppo.spring.cloud.s3.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        String filePath = "task01/01";
        int expectedSize = 1416;

        List<String> signedUrls = signedUrlService.createSignedUrl(filePath);

        assertThat(signedUrls).hasSize(expectedSize);
    }
}