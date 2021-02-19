package com.poppo.spring.cloud.s3.demo.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class S3LoaderTest {
    @Autowired
    private S3Loader s3Loader;

    @Test
    void pickStaticKeys() {
        String folderPath = "task01/01";
        int expectedSize = 1000;
        List<String> keys = s3Loader.pickStaticKeys(folderPath);

        assertThat(keys).hasSize(expectedSize);
    }
}