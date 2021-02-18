package com.poppo.spring.cloud.s3.demo.contorller;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.poppo.spring.cloud.s3.demo.infra.S3Loader;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final S3Loader s3Loader;

    public RestController(final S3Loader s3Loader) {
        this.s3Loader = s3Loader;
    }

    @GetMapping("/summary")
    public ResponseEntity getSummary() {
        List<S3ObjectSummary> fileSummary = s3Loader.getFileSummary();

        return ResponseEntity.ok(fileSummary);
    }

    @GetMapping("/summary/lazy")
    public ResponseEntity getSummary2() throws IOException {
        List<S3ObjectSummary> fileSummaryLazily = s3Loader.getFileSummaryLazily();

        return ResponseEntity.ok(fileSummaryLazily);
    }
}
