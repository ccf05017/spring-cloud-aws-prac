package com.poppo.spring.cloud.s3.demo.infra;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class S3Loader {
    private final AmazonS3Client amazonS3Client;

    public S3Loader(final AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<S3ObjectSummary> getFileSummary() {
        return amazonS3Client.listObjects(bucket)
                .getObjectSummaries();
    }

    public List<S3ObjectSummary> getFileSummaryLazily(String filePath) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(filePath)
                .withMaxKeys(2000);
        ListObjectsV2Result result;
        List<S3ObjectSummary> summaries = new ArrayList<>();

        do {
            result = amazonS3Client.listObjectsV2(request);
            summaries.addAll(result.getObjectSummaries());

            String token = result.getNextContinuationToken();
            request.setContinuationToken(token);
        } while (result.isTruncated());

        return summaries;
    }

    public List<S3ObjectSummary> pickStaticNumber(String filePath, int number) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(filePath)
                .withMaxKeys(number);

        return amazonS3Client.listObjectsV2(request)
                .getObjectSummaries();
    }

    public List<String> pickStaticFolder(String folderPath) {
        // 어차피 최대 1000개까지 밖에 설정 안됨.
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucket)
                .withPrefix(folderPath);

        return amazonS3Client.listObjectsV2(request)
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

    public String pickStaticObject(final String filePath) {
        return amazonS3Client.getObjectAsString(bucket, filePath);
    }
}
