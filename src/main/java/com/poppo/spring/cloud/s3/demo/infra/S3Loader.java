package com.poppo.spring.cloud.s3.demo.infra;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<S3ObjectSummary> getFileSummaryLazily() {
        try {
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucket)
                    .withMaxKeys(100);
            ListObjectsV2Result result;
            List<S3ObjectSummary> summaries = new ArrayList<>();

            do {
                result = amazonS3Client.listObjectsV2(request);
                summaries.addAll(result.getObjectSummaries());

                String token = result.getNextContinuationToken();
                request.setContinuationToken(token);
            } while (result.isTruncated());

            return summaries;
        } catch (SdkClientException e) {
            e.printStackTrace();;
        }
        return null;
    }
}
