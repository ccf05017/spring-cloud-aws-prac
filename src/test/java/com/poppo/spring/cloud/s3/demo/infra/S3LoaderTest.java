package com.poppo.spring.cloud.s3.demo.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class S3LoaderTest {
    @Autowired
    private S3Loader s3Loader;

    @Test
    void pickStaticFolderTest() {
        String folderPath = "task01/01";
        int expectedSize = 1000;
        String key2 = "task01/01/2020-05-10 17.28.52-2 복사본.jpg";
        String key5 = "task01/01/2020-05-10 18.20.57 복사본.jpg";
        String key6 = "task01/01/2020-05-10 18.20.57.jpg";
        List<String> keys = s3Loader.pickStaticFolder(folderPath);

        assertThat(keys).hasSize(expectedSize);
        assertThat(keys.get(1)).isEqualTo(key2);
        assertThat(keys.get(4)).isEqualTo(key5);
        assertThat(keys.get(5)).isEqualTo(key6);
    }

    @Test
    void pickStaticObjectTest() {
        String object = "task01/01/2020-05-10 17.28.52-2 복사본.jpg";

        String returned = s3Loader.pickStaticObject(object);

        assertThat(returned).isNotNull();
    }

    @DisplayName("오브젝트 1000개를 개별로 접근하는 테스트 - 5분 16초 정도 소요됨")
    @Test
    @Timeout(value = 600)
    void isRealFastTest() {
//        String folderPath = "task01/01";
//
//        List<String> keys = s3Loader.pickStaticFolder(folderPath);
//
//        keys.forEach(key -> assertThat(s3Loader.pickStaticObject(key)).isNotNull());
    }
}