package com.andela.irrigation_system.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.andela.irrigation_system.model.dto.S3SharedFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import static java.util.Optional.ofNullable;
import static org.springframework.util.MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 s3Client;

	@Value("${s3.bucket.name}")
	private String bucket;
	@Value("${assets.root.dir}")
	private String rootDir;

	public S3SharedFile generateUploadUrl(String fileName) {
		String uploadedName = ofNullable(s3Client.listObjects(bucket, Paths.get(rootDir).resolve(fileName).toString()))
				.map(list -> {
					long existingKeys = list.getObjectSummaries().stream()
							.filter(file -> file.getKey().endsWith(fileName)).count();
					return String.format("%s_%s%s", fileName.replace(".csv", ""), ++existingKeys, ".csv");
				}).orElse(fileName);

		GeneratePresignedUrlRequest uploadRequest = new GeneratePresignedUrlRequest(bucket, Paths.get(rootDir).resolve(uploadedName).toString())
				.withMethod(HttpMethod.PUT)
				.withContentType(APPLICATION_OCTET_STREAM_VALUE)
				.withExpiration(Date.from(Instant.now().plusSeconds(5 * 60)));
		return S3SharedFile.builder()
				.fileName(uploadedName)
				.presignedUrl(s3Client.generatePresignedUrl(uploadRequest).toString())
				.build();
	}

	public S3SharedFile generateDownloadUrl(String name) {
		GeneratePresignedUrlRequest downloadRequest = new GeneratePresignedUrlRequest(bucket, Paths.get(rootDir).resolve(name).toString())
				.withMethod(HttpMethod.GET)
				.withExpiration(Date.from(Instant.now().plusSeconds(3600)));
		return S3SharedFile.builder()
				.fileName(name)
				.presignedUrl(s3Client.generatePresignedUrl(downloadRequest).toString())
				.build();
	}

	public void deleteObject(String name) {
		s3Client.deleteObject(bucket, Paths.get(rootDir).resolve(name).toString());
	}

	void upload(String keyName, byte[] content) {
		InputStream is = new ByteArrayInputStream(content);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(content.length);
		metadata.setContentType(APPLICATION_OCTET_STREAM_VALUE);
		PutObjectRequest request = new PutObjectRequest(bucket, keyName, is, metadata);
		s3Client.putObject(request);
	}
}
