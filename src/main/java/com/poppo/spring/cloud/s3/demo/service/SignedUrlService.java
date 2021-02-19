package com.poppo.spring.cloud.s3.demo.service;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.poppo.spring.cloud.s3.demo.infra.S3Loader;
import com.poppo.spring.cloud.s3.demo.infra.S3SignedUrl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignedUrlService {
    private final S3Loader s3Loader;
    private final S3SignedUrl s3SignedUrl;

    public SignedUrlService(final S3Loader s3Loader, final S3SignedUrl s3SignedUrl) {
        this.s3Loader = s3Loader;
        this.s3SignedUrl = s3SignedUrl;
    }

    public List<String> createSignedUrl(String filePath) {
        List<S3ObjectSummary> fileSummaryLazily = s3Loader.getFileSummaryLazily(filePath);

        return fileSummaryLazily.stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}
