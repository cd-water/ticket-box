package com.cdwater.ticketbox.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioBucketInitializer implements ApplicationRunner {

    private static final String POLICY = """
            {
              "Version": "2012-10-17",
              "Statement": [{
                "Effect": "Allow",
                "Principal": {"AWS": ["*"]},
                "Action": ["s3:GetObject"],
                "Resource": ["arn:aws:s3:::%s/avatars/*"]
              }]
            }
            """;

    private final S3Client s3Client;
    private final MinioProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        String bucket = properties.getBucket();
        try {
            try {
                s3Client.headBucket(builder -> builder.bucket(bucket));
            } catch (NoSuchBucketException e) {
                s3Client.createBucket(builder -> builder.bucket(bucket));
            }
            s3Client.putBucketPolicy(builder -> builder
                    .bucket(bucket)
                    .policy(String.format(POLICY, bucket)));
            log.info("MinIO bucket ready: {}", bucket);
        } catch (Exception e) {
            log.error("MinIO bucket initialization failed", e);
        }
    }
}
