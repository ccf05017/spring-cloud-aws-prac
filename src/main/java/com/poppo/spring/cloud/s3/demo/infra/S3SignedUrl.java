package com.poppo.spring.cloud.s3.demo.infra;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;

@Component
public class S3SignedUrl {
    private final AmazonS3Client amazonS3Client;

    public S3SignedUrl(final AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public URL generateSignedUrl(String s3ObjectName) {
        amazonS3Client.generatePresignedUrl()
        return amazonS3Client.generatePresignedUrl(bucket, s3ObjectName, makeOneHourDurationFromNow());
    }

    interface ObjectList {
        fun getSignedUrlList(): List<String>
    }

    private Date makeOneHourDurationFromNow() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        return expiration;
    }
}
