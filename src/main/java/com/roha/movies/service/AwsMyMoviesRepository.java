package com.roha.movies.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roha.movies.domain.MyMovies;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;

@ApplicationScoped
public class AwsMyMoviesRepository implements MyMoviesRepository {

    private static final Logger log = LoggerFactory.getLogger(AwsMyMoviesRepository.class);
    String bucketName;
    String region;
    String key;
    String secret;

    public AwsMyMoviesRepository() {
    }

    public AwsMyMoviesRepository(@ConfigProperty(name = "s3_bucket") String bucketName) {
        this.bucketName = bucketName;
    }

    public AwsMyMoviesRepository(@ConfigProperty(name = "s3_bucket") String bucketName,
                                 @ConfigProperty(name = "s3_region") String region,
                                 @ConfigProperty(name = "access_key") String key,
                                 @ConfigProperty(name = "access_secret") String secret) {
        this.bucketName = bucketName;
        this.region = region;
        this.key = key;
        this.secret = secret;
    }

    public AwsMyMoviesRepository(@ConfigProperty(name = "s3_bucket") String bucketName,
                                 @ConfigProperty(name = "s3_region") String region) {
        this.bucketName = bucketName;
        this.region = region;
    }


    @Override
    public MyMovies load() {
        return loadMyMovies().orElse(new MyMovies());
    }

    public Optional<MyMovies> loadMyMovies() {
        Optional<MyMovies> myMovies = Optional.empty();

        if (bucketName != null) {
            S3Client s3 = createS3Client();
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key("my_movies.json")
                    .build();

            String myMoviesContent = s3.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                myMovies = Optional.of(objectMapper.readValue(myMoviesContent, MyMovies.class));
            } catch (JsonProcessingException e) {
                log.info("loaded the file but there was an json exception", e);
            }
        }
        return myMovies;
    }

    private S3Client createS3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(
                                key,
                                secret
                        )
                ))
                .region(Region.EU_CENTRAL_1)
                .build();
    }

    @Override
    public void save(MyMovies myMovies) {

        if (bucketName != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String json = objectMapper.writeValueAsString(myMovies);
                S3Client s3 = createS3Client();
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key("my_movies.json")
                        .build();

                s3.putObject(putObjectRequest, RequestBody.fromString(json));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void clean() {

    }
}
