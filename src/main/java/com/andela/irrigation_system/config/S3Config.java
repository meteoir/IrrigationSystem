package com.andela.irrigation_system.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
	
	@Value("${aws.region}")
	private String region;
	@Value("${aws.access.key}")
	private String accessKey;
	@Value("${aws.secret.key}")
	private String secretKey;
	@Value("${s3.endpoint}")
	private String endpoint;

	@Bean
	public AmazonS3 s3Client() {
		return AmazonS3Client.builder()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}
}
