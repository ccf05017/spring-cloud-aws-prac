package com.poppo.spring.cloud.s3.demo.contorller;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.poppo.spring.cloud.s3.demo.infra.S3Loader;
import com.poppo.spring.cloud.s3.demo.infra.S3SignedUrl;
import com.poppo.spring.cloud.s3.demo.infra.S3Uploader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final S3Loader s3Loader;
    private final S3SignedUrl signedUrl;

    public RestController(final S3Loader s3Loader, final S3SignedUrl signedUrl) {
        this.s3Loader = s3Loader;
        this.signedUrl = signedUrl;
    }

    @GetMapping("/summary")
    public ResponseEntity getSummary() {
        List<S3ObjectSummary> fileSummary = s3Loader.getFileSummary();

        return ResponseEntity.ok(fileSummary);
    }

    @GetMapping("/summary/lazy")
    public ResponseEntity getSummary2() throws IOException {
        String filePath = "temp";
        List<S3ObjectSummary> fileSummaryLazily = s3Loader.getFileSummaryLazily(filePath);

        return ResponseEntity.ok(fileSummaryLazily);
    }

    @PostMapping("/signed-url")
    public ResponseEntity createSignedUrl(@RequestBody SignedUrlRequest request) throws URISyntaxException {
        URL url = signedUrl.generateSignedUrl(request.getS3ObjectName());

        return ResponseEntity.created(url.toURI()).build();
    }
}
