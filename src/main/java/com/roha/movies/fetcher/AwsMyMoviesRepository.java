package com.roha.movies.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roha.movies.domain.MyMovies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AwsMyMoviesRepository implements MyMoviesRepository {

    String bucketName;
    String region;

    public AwsMyMoviesRepository() {
    }

    public AwsMyMoviesRepository(@Value("s3_bucket") String bucketName) {
        this.bucketName = bucketName;
    }

    public AwsMyMoviesRepository(@Value("s3_bucket") String bucketName,@Value("s3_region") String region) {
        this.bucketName = bucketName;
        this.region = region;
    }

    @Override
    public MyMovies load() {
        final Bucket bucket = getBucket(bucketName);
        MyMovies myMovies = null;

        if (bucket != null) {
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
            S3Object s3Object = s3.getObject(bucketName, "my_movies.json");
            try {
                String fileContent = new String(s3Object.getObjectContent().readAllBytes(), "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                myMovies = objectMapper.readValue(fileContent, MyMovies.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return myMovies;
    }

    public Bucket getBucket(String bucket_name) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        Bucket named_bucket = null;
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            if (b.getName().equals(bucket_name)) {
                named_bucket = b;
            }
        }
        return named_bucket;
    }

    @Override
    public void save(MyMovies myMovies) {
        final Bucket bucket = getBucket(bucketName);

        if (bucket != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String json = objectMapper.writeValueAsString(myMovies);
                final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
                s3.putObject(bucketName, "my_movies.json", json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void clean() {

    }
}
