package com.poppo.spring.cloud.s3.demo.contorller;

public class SignedUrlRequest {
    private String s3ObjectName;

    public SignedUrlRequest() {
    }

    public SignedUrlRequest(final String s3ObjectName) {
        this.s3ObjectName = s3ObjectName;
    }

    public String getS3ObjectName() {
        return s3ObjectName;
    }

    public void setS3ObjectName(final String s3ObjectName) {
        this.s3ObjectName = s3ObjectName;
    }
}
